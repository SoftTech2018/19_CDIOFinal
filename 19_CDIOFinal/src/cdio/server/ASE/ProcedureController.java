package cdio.server.ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import cdio.server.DAL.IControllerDAO;
import cdio.server.DAL.dto.ReceptKompDTO;
import cdio.shared.DALException;
import cdio.shared.ProduktBatchKompDTO;

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
//			System.exit(1);
		} catch (IOException e) {
			System.out.println("Fejl ved forbindelse til vaegten. Programmet lukket.");
//			System.exit(1);
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
					System.out.println("rdln1: "+in.readLine());
				} else if (test.equalsIgnoreCase("l")){
//					System.out.println("rdln2"+in.readLine());					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
					input = trans.RM20("Tast bruger ID:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					inputInt = Integer.parseUnsignedInt(input);
					name = dao.getOprDAO().getOperatoer(inputInt).getName();
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
//						trans.RM20cancel();
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
						System.exit(1);
					}
					return START;
				} catch (DALException e) {
					// TODO Auto-generated catch block
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
					input = trans.RM20("Tast produktbatch nr.:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					mc.setProdBatchID(Integer.parseUnsignedInt(input));
					try{
						dao.getPbDAO().getProduktBatch(mc.prod_batch_id);
					} catch (DALException e){
						trans.P111("Ukendt nr; tast nyt.");
						return SETUP;
					}
					if(!dao.getPbKompDAO().getProduktBatchKompList(mc.prod_batch_id).isEmpty()){
						trans.P111("Nr er brugt; tast nyt.");
						return SETUP;
					}
					product = dao.getReceptDAO().getRecept(dao.getPbDAO().getProduktBatch(mc.getProdBatchID()).getReceptId()).getReceptNavn();
					menu.show("Produkt valgt: "+product+". Er dette korrekt?");
					prodInput = trans.RM20("Bekraft produkt:",product," ?");
					if (prodInput.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					if(prodInput.equals(product)){
						menu.show("Produkt bekraftet.");
						mc.setReceptID(dao.getPbDAO().getProduktBatch(mc.getProdBatchID()).getReceptId());
						mc.setReceptKompListe(dao.getReceptKompDAO().getReceptKompList(mc.getReceptID()));
						dao.getPbDAO().getProduktBatch(mc.getProdBatchID()).setStatus(2);
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
//						System.exit(1);
					}
					return SETUP;
				} catch (IOException e){
					try {
						menu.show("Produkt findes ikke. Prov igen.");
						trans.RM20("Produkt findes ikke. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
//						System.exit(1);
					}
					return SETUP;
				} catch (DALException e) {
					try {
						trans.P111("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
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
					menu.show("Ingen belastning, bekraft.");
					input = trans.RM20("Fjern belastning!","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.T(); //Simulator svarer ikke korrekt på tarering
					menu.show("Pasat beholder og bekraft.");
					input = trans.RM20("Pasat beholder, bekraft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					if (input.toUpperCase().equals(answer)) {
						menu.show("Beholder pasat");
						mc.setTara(Double.parseDouble(trans.T())); //Simulator svarer ikke korrekt på tarering
//						mc.setTara(Double.parseDouble("0.555"));
						menu.show("Vagt tareret: "+mc.getTara());
						mc.setReceptKomp(mc.getReceptKompListe().remove(0));
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
//						System.exit(1);
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
				String input = null, answer = "OK",raavare,raavareInput;
				int inputInt = 0;
				try{
//					menu.show("Indtast varenummer:");
//					input = trans.RM20("Tast raavarebatch nr:","","");
//					menu.show(input);
//					if(input.toLowerCase().equals("q")){
//						menu.show("Proceduren afbrudt af brugeren");
//						trans.P111("");
//						return START;
//					}
//					trans.P111("");
//					inputInt = Integer.parseUnsignedInt(input);
//					raavare = dao.getRaavareDAO().getRaavare(dao.getRbDAO().getRaavareBatch(inputInt).getRaavareId()).getRaavareNavn();
//					menu.show("Produkt valgt: "+raavare+". Er dette korrekt?");
//					raavareInput = trans.RM20("Bekraft produkt:",raavare," ?");
//					if (raavareInput.toLowerCase().equals("q")){
//						menu.show("Proceduren afbrudt af brugeren");
//						trans.P111("");
//						return START;
//					}
//					if(raavareInput.equals(raavare)){
//						menu.show("Produkt bekraftet.");
//						mc.setRaavareID(inputInt);
//					} else {
//						menu.show("Forkert produkt. Prov igen.");
//						trans.RM20("Forkert produkt. Prov igen.", "OK", "?");
//						return WEIGH;
//					}
					
					menu.show("Afvej vare og bekraft.");
					input = trans.RM20("Afvej "+dao.getRaavareDAO().getRaavare(mc.getRaavareID()).getRaavareNavn()+", bekraft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					if (input.toUpperCase().equals(answer)) {
						menu.show("Afvej og kvitter med dor-knap");
						trans.P111("Afvej "+mc.getReceptKomp().getNomNetto()+"g");
						trans.startST(true);
						mc.setAfvejning(Double.parseDouble(trans.listenST()));
						trans.startST(false);
						menu.show(mc.getAfvejning()+" afvejet.");
						trans.P111("");
						if(mc.getAfvejning()>=(mc.getAfvejning()-mc.getReceptKomp().getTolerance()) && mc.getAfvejning()<=(mc.getAfvejning()+mc.getReceptKomp().getTolerance())){
							return REMOVE_CONTAINER;
						} else {
							trans.P111("Afvejet uden for tolerancen");
							return WEIGH;
						}
					} else {
						menu.show("Vare ej afvejet. Prov igen.");
						trans.RM20("Vare ej afvejet. Prov igen.", "OK", "?");
						return WEIGH;
					}

				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.RM20("Fejl. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
//						System.exit(1);
					}
					return WEIGH;
				} catch (DALException e) {
					// TODO Auto-generated catch block
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
//						fileAccess.updProductInventory(mc.getVareID(), mc.getAfvejning());
//						menu.show("Beholdning opdateret:");
//						menu.show("Vare ID: "+mc.getVareID()+", Afvejning: "+mc.getAfvejning());

						dao.getPbKompDAO().createProduktBatchKomp(new ProduktBatchKompDTO(mc.prod_batch_id, mc.raavare_id, mc.getTara(), mc.getAfvejning(), mc.getOprID()));
						if(mc.getReceptKompListe().isEmpty()){
							dao.getPbDAO().getProduktBatch(mc.getProdBatchID()).setStatus(3);
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
//						System.exit(1);
					}
					return REMOVE_CONTAINER;
				} catch (DALException e) {
					e.printStackTrace();
					return REMOVE_CONTAINER;
				}
			}
		},
//		RESTART {
//
//			@Override
//			String desc() {
//				return "State: RESTART";
//			}
//
//			@Override
//			State changeState(IProcedure menu, IControllerDAO dao, ITransmitter trans, ProcedureController mc) {
//				String input = null, answer = "OK";
//				try{
//					menu.show("Foretag ny vejning?");
//					input = trans.RM20("Foretag ny vejning?","OK","");
//					menu.show(input);
//					trans.P111("");
//					if (input.equals(answer)) {
//						menu.show("Proceduren genstartes.");
//						return SETUP;
//					} else {
//						menu.show("Proceduren afbrudt af brugeren.");
//						return START;
//					}
//
//				} catch (NumberFormatException | IOException e) {
//					try {
//						menu.show("Fejl. Prov igen.");
//						trans.RM20("Fejl. Prov igen", "OK", "?");
//					} catch (IOException e1) {
//						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
//						System.exit(1);
//					}
//					return RESTART;
//				}
//			}			
//		},
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

}
