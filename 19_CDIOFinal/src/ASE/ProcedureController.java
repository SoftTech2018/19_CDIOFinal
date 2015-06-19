package ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.ReceptKompDTO;

public class ProcedureController implements Runnable, IProcedureController {

	private State state;
	private IProcedure menu;
	private ITransmitter trans;
	private IControllerDAO dao;
	private int opr_id,raavare_id,recept_id,prod_batch_id,rb_ID;
	private double afvejning,tara;
	private String host;
	private List<ReceptKompDTO> receptKompListe;
	private List<ProduktBatchKompDTO> pbKompListe;
	private List<ReceptKompDTO> restListe;
	private ReceptKompDTO receptKomp;
	private BufferedReader in;
	private Socket socket;

	public ProcedureController(Socket socket, IProcedure menu, IControllerDAO dao, ITransmitter trans) {
		this.menu = menu;
		this.trans = trans;
		this.dao = dao;
		this.host = socket.getInetAddress().getHostAddress();
		this.socket = socket;
		this.state = State.START;
	}


	@Override
	public void run() {
		connect(socket);		
	}

	@Override
	public void connect(Socket socket){
		try (	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
			trans.connected(in, out);
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
			try {
				String test = trans.RM20("123456789012345678901234567890", "", "");
				System.out.println("Test: "+test);
				if(test.equalsIgnoreCase("es")){
					in.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
				this.state = State.STOP;
			}
			do{
			menu.show("");
			menu.show(state.desc());
			this.state = this.state.changeState(menu,dao,trans,this);		
		}
		while(!state.equals(State.STOP));
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("PROGRAMMET AFSLUTTET!!!!! ASFUAN");
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
					//					input = trans.RM20("Tast bruger ID:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					inputInt = Integer.parseInt(input);
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
				}
					catch (SocketException e){ 
						System.out.println("Ingen forbindelse til vægten!");
						e.printStackTrace();
						return STOP;
				} catch (NumberFormatException e) {
					try {
						menu.show("Forkert input type. Prov igen.");
						trans.RM20("Forkert input type. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("IOException fejl");
						return STOP;
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
				mc.pbKompListe=null;
				mc.receptKompListe=null;
				mc.restListe=null;
				mc.restListe = new ArrayList<ReceptKompDTO>();
				try{
					menu.show("Indtast varenummer:");
					input = trans.RM20int("Tast produktbatch nr.:","","");
					//					input = trans.RM20("Tast produktbatch nr.:","","");
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
					if(dao.getProduktBatch(mc.prod_batch_id).getStatus()==2){
						trans.P111("Nr er brugt; tast nyt.");
						return SETUP;
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
						mc.setReceptID(dao.getProduktBatch(mc.getProdBatchID()).getReceptId());
						mc.receptKompListe=dao.getReceptKompListe(mc.recept_id);
						for(ReceptKompDTO rk : mc.receptKompListe){
							if(!mc.restListe.contains(rk)){
								mc.restListe.add(rk);
							}
						}
						mc.pbKompListe=dao.getPBKList(mc.prod_batch_id);
						return CHECK;
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
		CHECK {
			@Override
			String desc() {
				return "State: CHECK";
			}

			@Override
			State changeState(IProcedure menu, IControllerDAO dao,ITransmitter trans, ProcedureController mc) {
				String in;
				int input,test;
				try {
					mc.setReceptKomp(mc.restListe.get(0));
					mc.setRaavareID(mc.getReceptKomp().getRaavareId());
					if(dao.getProduktBatch(mc.getProdBatchID()).getStatus()==1){
						for(ProduktBatchKompDTO pbk : mc.pbKompListe){
							if(mc.raavare_id==dao.getRaaID(pbk.getRbId())){
								mc.restListe.remove(0);
								return CHECK;
							}
						}						
					}
					trans.P111("");
					in = trans.RM20int(dao.getSpecificRaavare(mc.raavare_id)+" batch nr", "", "");
					//					in = Integer.parseInt(trans.RM20(dao.getSpecificRaavare(mc.raavare_id)+" batch nr", "", ""));
					if(in.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					input = Integer.parseInt(in);
					System.out.println(mc.raavare_id);
					System.out.println(input);
					test=dao.getRbDAO().getRaavareBatch(input).getRaavareId();
					System.out.println(test);
					if(test==mc.raavare_id){
						mc.rb_ID=input;
						if(dao.getReceptKomp(mc.recept_id, mc.raavare_id).getNomNetto()>=dao.getRbDAO().getRaavareBatch(mc.rb_ID).getMaengde()){
							trans.P111("Batch for lille.");
							return CHECK;
						}
						return CLEAR;
					} else {
						trans.P111("ID og batch nr matcher ikke");
						return CHECK;						
					}

				} catch (NumberFormatException | IOException | DALException e) {
					return CHECK;
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
						return WEIGH;
					} else {
						menu.show("Beholder ej pasat. Prov igen.");
						trans.RM20("Beholder ej pasat. Prov igen.", "OK", "?");
						return CLEAR;
					}
				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.P111("Fejl. Prov igen.");
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
						dao.createProduktBatchKomp(new ProduktBatchKompDTO(mc.prod_batch_id, mc.rb_ID, mc.getTara(), mc.getAfvejning(), mc.getOprID(),mc.getHost()));
						RaavareBatchDTO p = dao.getRbDAO().getRaavareBatch(mc.rb_ID);
						p.setMaengde(p.getMaengde()-mc.afvejning);
						dao.updateRbMaengde(p);
						mc.restListe.remove(0);
						if(mc.restListe.isEmpty()){
							dao.updatePbStatus(mc.prod_batch_id, 2);
							dao.setTimeStamp(mc.getProdBatchID(), 1, mc.prettyTime());
							trans.RM20("Afvejning faerdig!", "OK", "");
							return START;
						} else {
							return CHECK;
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

	private void setReceptID(int id){
		this.recept_id=id;
	}

	private int getProdBatchID(){
		return prod_batch_id;
	}

	private void setProdBatchID(int id){
		this.prod_batch_id=id;
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
