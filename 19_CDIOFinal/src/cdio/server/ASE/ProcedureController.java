package cdio.server.ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.ReceptKompDTO;

public class ProcedureController implements Runnable, IProcedureController {

	private State state;
	private IProcedure menu;
	private ITransmitter trans;
	private IControllerDAO dao;
	private int opr_id,raavare_id,recept_id,prod_batch_id;
	private double afvejning,tara;
	private int port;
	private String host;
	private List<ReceptKompDTO> receptKompListe;
	private List<ProduktBatchKompDTO> pbKompListe;
	private List<ReceptKompDTO> restListe;
	private ReceptKompDTO receptKomp;
	private PrintWriter out;
	private BufferedReader in;

	public ProcedureController(IProcedure menu, IControllerDAO dao, String host, int port, ITransmitter trans) {
		this.menu = menu;
		this.trans = trans;
		this.dao = dao;
		this.host = host;
		this.port = port;
		this.state = State.START;
	}


	@Override
	public void run() {
		connect(host, port);		
	}

	@Override
	public void connect(String host, int port){
		try (Socket	socket = new Socket(host, port);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
			trans.connected(in, out);
			this.out=out;
			this.in=in;
			start();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException fejl");
		} catch (IOException e) {
			System.out.println("Fejl ved forbindelse til vaegten. Programmet lukket.");
		}
	}

	/* (non-Javadoc)
	 * @see wcuMain.IMenuController#start()
	 */
	@Override
	public void start(){
		menu.show("Overvagning af vagtbetjening");
		do{
			try {
				String test = trans.RM20("123456789012345678901234567890", "", "");
				System.out.println("Test: "+test);
				if(test.equalsIgnoreCase("es")){
					in.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			menu.show("");
			menu.show(state.desc());
			this.state = this.state.changeState(menu,dao,trans,this);		
		}
		while(!state.equals(State.STOP));
	}

	public enum State {
		START {
			@Override
			String desc(){				
				return "State: START"; 
			}
			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null,name,nameInput;
				int inputInt = 0;
				try{
					menu.show("Indtast operatornummer:");
					input = trans.RM20int("Tast bruger ID:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					inputInt = Integer.parseUnsignedInt(input);
					if(!dao.isOpr(inputInt)){
						trans.P111("Uautoriseret bruger");
						return START;
					}
					name = dao.getUser(inputInt).getName();
					menu.show("Bruger valgt: "+name+". Er dette korrekt?");		
					nameInput = trans.RM20("Bekraft bruger:",name," ?");
					if (nameInput.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					if(nameInput.equals(name)) {
						menu.show(nameInput+" bekraftet.");
						mc.setOprID(inputInt);
						return SETUP;
					} else {
						menu.show(nameInput);
						menu.show("Forkert bruger. Prov igen.");
						trans.RM20("Forkert bruger. Prov igen.", "OK", "?");
						return START;
					}
				} catch (NumberFormatException e) {
					try {
						menu.show("Forkert input type. Prov igen.");
						trans.RM20("Forkert input type. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("IOException fejl");
						System.exit(1);
					}
					return START;
				} catch (IOException e){
					try {
						menu.show("Bruger findes ikke. Prov igen.");
						trans.RM20("Bruger findes ikke. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
					}
					return START;
				} catch (DALException e) {
					e.printStackTrace();
					return START;
				}
			}
		},
		SETUP {
			@Override
			String desc() {
				return "State: SETUP";
			}
			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, product, prodInput;
				try{
					menu.show("Indtast varenummer:");
					input = trans.RM20int("Tast produktbatch nr.:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					mc.setProdBatchID(Integer.parseInt(input));
					try{
						dao.getProduktBatch(mc.prod_batch_id);
					} catch (DALException e){
						trans.P111("Ukendt nr; tast nyt.");
						return SETUP;
					}
					mc.restListe = new ArrayList<ReceptKompDTO>();
					mc.setReceptID(dao.getProduktBatch(mc.getProdBatchID()).getReceptId());
					mc.receptKompListe=dao.getReceptKompListe(mc.recept_id);
					for(ReceptKompDTO rk : mc.receptKompListe){
						mc.restListe.add(mc.receptKompListe.get(mc.receptKompListe.indexOf(rk)));
					}
					boolean tom = dao.getProduktBatchKompListIsEmpty(mc.prod_batch_id);
					System.out.println(tom);
					if(!tom){
						int pos;
						mc.pbKompListe=dao.getPBKList(mc.prod_batch_id);
						System.out.println("");
						System.out.println(mc.restListe.size());
						for(ReceptKompDTO rk : mc.getReceptKompListe()){
							pos = mc.restListe.indexOf(rk);
							for(ProduktBatchKompDTO pbk : mc.getpbKompListe()){
								if(rk.getRaavareId()==dao.getRaaID(pbk.getRbId())){
									System.out.println(pos);
									mc.restListe.remove(pos);
								}
							}
						}
						System.out.println("");
						System.out.println(mc.restListe.size());
						if(mc.restListe.isEmpty()){
							trans.P111("Nr er brugt; tast nyt.");
							return SETUP;							
						}
					}
					product = dao.getReceptName(mc.getProdBatchID());
					menu.show("Produkt valgt: "+product+". Er dette korrekt?");
					prodInput = trans.RM20("Bekraft recept:",product," ?");
					if (prodInput.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					if(prodInput.equals(product)){
						menu.show("Produkt bekraftet.");
						if(dao.getProduktBatch(mc.getProdBatchID()).getStatus()==0){
							dao.updatePbStatus(mc.getProdBatchID(), 1);
							dao.setTimeStamp(mc.getProdBatchID(), 0, mc.prettyTime());							
						}
						return CLEAR;
					} else {
						menu.show("Forkert produkt. Prov igen.");
						trans.RM20("Forkert produkt. Prov igen.", "OK", "?");
						return SETUP;
					}
				} catch (NumberFormatException e) {
					try {
						menu.show("Forkert input type. Prov igen.");
						trans.RM20("Forkert input type. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("IOException fejl");
					}
					return SETUP;
				} catch (IOException e){
					try {
						menu.show("Produkt findes ikke. Prov igen.");
						trans.RM20("Produkt findes ikke. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
					}
					return SETUP;
				} catch (DALException e) {
					try {
						trans.P111("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
					return SETUP;
				}				
			}
		},
		CLEAR {
			@Override
			String desc() {
				return "State: SET_CONTAINER";
			}

			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, answer = "OK";
				try{
					menu.show("Pasat beholder og bekraft.");
					input = trans.RM20("Pasat beholder, bekraft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					trans.T();
					if (input.toUpperCase().equals(answer)) {
						menu.show("Beholder pasat");
						mc.setTara(Double.parseDouble(trans.T()));
						menu.show("Vagt tareret: "+mc.getTara());
						mc.setReceptKomp(mc.restListe.get(0));
						mc.setRaavareID(mc.getReceptKomp().getRaavareId());
						return WEIGH;
					} else {
						menu.show("Beholder ej pasat. Prov igen.");
						trans.RM20("Beholder ej pasat. Prov igen.", "OK", "?");
						return CLEAR;
					}
				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.RM20("Fejl. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
					}
					return CLEAR;
				}				
			}
		},
		WEIGH {
			@Override
			String desc() {
				return "State: ADD_PRODUCT";
			}
			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				try{
					trans.P111("");
					menu.show("Afvej og kvitter med dor-knap");
					trans.P111("Afvej "+dao.getSpecificRaavare(mc.getRaavareID())+" : "+mc.getReceptKomp().getNomNetto()+"kg");
					trans.startST(true);
					mc.setAfvejning(Double.parseDouble(trans.listenST()));
					trans.startST(false);
					menu.show(mc.getAfvejning()+" afvejet.");
					trans.P111("");
					double tolerance = mc.getReceptKomp().getNomNetto()*mc.getReceptKomp().getTolerance();
					double min = mc.getReceptKomp().getNomNetto()-tolerance;
					double max = mc.getReceptKomp().getNomNetto()+tolerance;
					if(mc.getAfvejning()<min || mc.getAfvejning()>max){
						trans.P111("Afvejet uden for tolerancen");
						return WEIGH;
					} else {
						return REMOVE_CONTAINER;
					}
				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.RM20("Fejl. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
					}
					return WEIGH;
				} catch (DALException e) {
					e.printStackTrace();
					return WEIGH;
				}
			}
		},
		REMOVE_CONTAINER {
			@Override
			String desc() {
				return "State: REMOVE_CONTAINER";
			}
			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, answer = "OK";
				try{
					menu.show("Fjern beholder og bekraft.");
					input = trans.RM20("Fjern beholder, kvitter:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					if (input.equals(answer)) {
						dao.createProduktBatchKomp(new ProduktBatchKompDTO(mc.prod_batch_id, mc.raavare_id, mc.getTara(), mc.getAfvejning(), mc.getOprID(),mc.getHost()));
						mc.restListe.remove(0);
						if(mc.restListe.isEmpty()){
							dao.updatePbStatus(mc.prod_batch_id, 2);
							dao.setTimeStamp(mc.getProdBatchID(), 1, mc.prettyTime());
							trans.RM20("Afvejning faerdig!", "OK", "");
							return START;
						} else {
							return CLEAR;
						}
					} else {
						menu.show("Beholder ej fjernet. Prov igen.");
						trans.RM20("Beholder ej fjernet. Prov igen", "OK", "?");
						return REMOVE_CONTAINER;
					}

				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.RM20("Fejl. Prov igen", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
					}
					return REMOVE_CONTAINER;
				} catch (DALException e) {
					e.printStackTrace();
					return REMOVE_CONTAINER;
				}
			}
		},
		STOP {
			@Override
			String desc() {
				return null;
			}

			@Override
			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
				return STOP;
			}
		};
		abstract State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc);
		abstract String desc();		
	}

	private int getOprID(){
		return opr_id;
	}

	private void setOprID(int id){
		this.opr_id=id;
	}

	private int getRaavareID(){
		return raavare_id;
	}

	private void setRaavareID(int id){
		this.raavare_id=id;
	}

	private int getReceptID(){
		return recept_id;
	}

	private void setReceptID(int id){
		this.recept_id=id;
	}

	private int getProdBatchID(){
		return prod_batch_id;
	}

	private void setProdBatchID(int id){
		this.prod_batch_id=id;
	}

	private List<ReceptKompDTO> getReceptKompListe(){
		return receptKompListe;
	}

	private void setReceptKompListe(List<ReceptKompDTO> liste){
		this.receptKompListe=liste;
	}

	private List<ProduktBatchKompDTO> getpbKompListe(){
		return pbKompListe;
	}

	private ReceptKompDTO getReceptKomp(){
		return receptKomp;
	}

	private void setReceptKomp(ReceptKompDTO receptKomp){
		this.receptKomp=receptKomp;
	}

	private double getAfvejning(){
		return afvejning;
	}

	private void setAfvejning(double afvejning){
		this.afvejning=afvejning;
	}

	private double getTara(){
		return tara;
	}

	private void setTara(double tara){
		this.tara=tara;
	}

	private String getHost(){
		return this.host;
	}

	private void setHost(String host){
		this.host=host;
	}

	private String prettyTime(){ return prettyTime(System.currentTimeMillis()); }
	private String prettyTime(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		int sec = cal.get(Calendar.SECOND);
		String _sec = String.format("%02d", sec);
		int min = cal.get(Calendar.MINUTE);
		String _min = String.format("%02d", min);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String _hour = String.format("%02d", hour);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String _day = String.format("%02d", day);
		int month = cal.get(Calendar.MONTH) +1;
		String _month = String.format("%02d", month);
		int year = cal.get(Calendar.YEAR);
		String _year = Integer.toString(year);
		String time = _year+"/"+_month+"/"+_day+" - "+_hour+":"+_min+":"+_sec;
		return time;
	}

}
