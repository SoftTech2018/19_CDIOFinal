package ASE;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;

public class ServerASE implements Runnable{

	private List<String> ip;
	static List<Thread> active;
	private List<String> activeIp;
	private int port, portCheck;

	public ServerASE(){
		ip = new ArrayList<String>();
		active = new ArrayList<Thread>();
		activeIp = new ArrayList<String>();
		port = 8000; // Hvilken port benyttes til afvejningsproceduren
		portCheck = 8000; // Hvilken port benyttes til at tjekke om vægten er tændt (23, 80, 8000?)

		/*
		 * Tilføj de vægtes IP-adresser der skal tjekkes.
		 */
		//		ip.add("169.254.2.2");
		//		ip.add("169.254.2.3");
		ip.add("192.168.1.24");
		ip.add("localHost");
		ip.add("192.168.1.213");
	}

	@Override
	public void run() {
		while (true){
			try {
				for (int p=0; p<active.size(); p++){

					/*
					 * Tjek om tråden (afvejningsproceduren) stadig er aktiv.
					 * Hvis ikke fjernes den fra listen over aktive, og tilføjes igen til listen der forsøges at tilsluttes til
					 */
					if (!active.get(p).isAlive()){
						System.out.println("Afvejningsprocedure afsluttet på ip: " + activeIp.get(p));
						ip.add(activeIp.get(p)); 
						active.remove(p);
						activeIp.remove(p);
					}
				}

				for (int i=0; i<ip.size(); i++){
					/*
					 *  Tjek om der kan oprettes forbindelse til IP på port 7 (echo). 
					 *  I så fald oprettes en afvejningsprocedure på vægten.
					 */
					Socket w = null;
					try {
						w = new Socket();
						w.connect(new InetSocketAddress(ip.get(i), portCheck), 1000);  // 1000 = 1 sek til at connecte
					} catch (SocketTimeoutException e){
						System.out.println("Kunne ikke forbinde til: " + ip.get(i));
					}
					if (w.isConnected()){
						w.close(); // Luk socket så ProcedureController kan oprette sin egen socket
						IProcedure menu = new Procedure();
						ITransmitter trans = new Transmitter();
						IControllerDAO dao = new ControllerDAO(0);

						IProcedureController ase = new ProcedureController(menu, dao , ip.get(i), port, trans);
						Thread t = new Thread((Runnable) ase);
						t.start();

						active.add(t);
						activeIp.add(ip.get(i));
						System.out.println("Afvejningsprocedure startet på ip: " + ip.get(i));
						ip.remove(i); 
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DALException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


