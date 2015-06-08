package cdio.server.ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import cdio.server.DAL.DALException;
import cdio.server.DAL.DTO;
import cdio.server.DAL.IDAO;

public class ProcedureController implements Runnable, IProcedureController {

	private State state;
	private IProcedure menu;
	private ITransmitter trans;
	private IDAO dao;
	private int opr_nr,vare_nr;
	private double afvejning,tara;
	private int port;
	private String host;

	public ProcedureController(IProcedure menu, IDAO dao, String host, int port, ITransmitter trans) {
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
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null,name,nameInput;
				int inputInt = 0;
				try{
					menu.show("Indtast operatornummer:");
					input = trans.RM20("Tast bruger ID (10-16):","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					inputInt = Integer.parseUnsignedInt(input);
					name = dao.getOprDAO().getOperatoer(inputInt).getNavn();
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
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, product, prodInput;
				int inputInt = 0;
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
					inputInt = Integer.parseUnsignedInt(input);
					product = fileAccess.getProductName(inputInt);
					menu.show("Produkt valgt: "+product+". Er dette korrekt?");
					prodInput = trans.RM20("Bekraft produkt:",product," ?");
					if (prodInput.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					if(prodInput.equals(product)){
						menu.show("Produkt bekraftet.");
						mc.setVareID(inputInt);
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
						System.exit(1);
					}
					return SETUP;
				} catch (IOException e){
					try {
						menu.show("Produkt findes ikke. Prov igen.");
						trans.RM20("Produkt findes ikke. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
						System.exit(1);
					}
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
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, answer = "OK";
				try{
					menu.show("Pasat beholder og bekraft.");
					input = trans.RM20("Ingen belastning, bekraeft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.T();
					menu.show("Pasat beholder og bekraft.");
					input = trans.RM20("Pasat beholder, bekraft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					if (input.equals(answer)) {
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
						trans.RM20("Fejl. Prov igen.", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
						System.exit(1);
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
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, answer = "OK",raavare,raavareInput;
				int inputInt = 0;
				try{
					menu.show("Indtast varenummer:");
					input = trans.RM20("Tast raavarebatch nr.:","","");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					inputInt = Integer.parseUnsignedInt(input);
					raavare = fileAccess.getProductName(inputInt);
					menu.show("Produkt valgt: "+raavare+". Er dette korrekt?");
					raavareInput = trans.RM20("Bekraft produkt:",raavare," ?");
					if (raavareInput.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					if(raavareInput.equals(raavare)){
						menu.show("Produkt bekraftet.");
						mc.setVareID(inputInt);
					} else {
						menu.show("Forkert produkt. Prov igen.");
						trans.RM20("Forkert produkt. Prov igen.", "OK", "?");
						return WEIGH;
					}		
					menu.show("Afvej vare og bekraft.");
					input = trans.RM20("Afvej vare og bekraft:","OK","?");
					menu.show(input);
					if(input.toLowerCase().equals("q")){
						menu.show("Proceduren afbrudt af brugeren");
						trans.P111("");
						return START;
					}
					trans.P111("");
					if (input.equals(answer)) {
						menu.show("Afvej og kvitter med dor-knap");
						trans.P111("Afvej og kvitter med dor-knap");
						trans.startST(true);
						mc.setAfvejning(Double.parseDouble(trans.listenST()));
						trans.startST(false);
						menu.show(mc.getAfvejning()+" afvejet.");
						trans.P111("");
						return REMOVE_CONTAINER;
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
						System.exit(1);
					}
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
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
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
						fileAccess.updProductInventory(mc.getVareID(), mc.getAfvejning());
						menu.show("Beholdning opdateret:");
						menu.show("Vare ID: "+mc.getVareID()+", Afvejning: "+mc.getAfvejning());
						fileAccess.writeLog(mc.getOprID(), mc.getVareID(), mc.getTara(), mc.getAfvejning());
						menu.show("Log skrevet:");
						menu.show("Operator ID: "+mc.getOprID()+", Vare ID: "+mc.getVareID()+", Tara vagt: "+mc.getTara()+", Afvejning: "+mc.getAfvejning());
						return RESTART;
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
						System.exit(1);
					}
					return REMOVE_CONTAINER	;
				}
			}
		},
		RESTART {

			@Override
			String desc() {
				return "State: RESTART";
			}

			@Override
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				String input = null, answer = "OK";
				try{
					menu.show("Foretag ny vejning?");
					input = trans.RM20("Foretag ny vejning?","OK","");
					menu.show(input);
					trans.P111("");
					if (input.equals(answer)) {
						menu.show("Proceduren genstartes.");
						return SETUP;
					} else {
						menu.show("Proceduren afbrudt af brugeren.");
						return START;
					}

				} catch (NumberFormatException | IOException e) {
					try {
						menu.show("Fejl. Prov igen.");
						trans.RM20("Fejl. Prov igen", "OK", "?");
					} catch (IOException e1) {
						System.out.println("Fejl ved forbindelse til vagten. Programmet lukket.");
						System.exit(1);
					}
					return RESTART;
				}
			}			
		},
		STOP {
			@Override
			String desc() {
				return null;
			}

			@Override
			State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc) {
				return STOP;
			}
		};
		abstract State changeState(IProcedure menu, IDAO dao, ITransmitter trans, ProcedureController mc);
		abstract String desc();		
	}

	private int getOprID(){
		return opr_nr;
	}

	private void setOprID(int id){
		this.opr_nr=id;
	}

	private int getVareID(){
		return vare_nr;
	}

	private void setVareID(int id){
		this.vare_nr=id;
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
