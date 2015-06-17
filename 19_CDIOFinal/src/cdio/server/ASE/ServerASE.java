package cdio.server.ASE;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;

public class ServerASE implements Runnable{

	private List<String> ip;
	private List<Thread> active;
	private List<String> activeIp;
	private int port;

	public ServerASE(){
		ip = new ArrayList<String>();
		active = new ArrayList<Thread>();
		activeIp = new ArrayList<String>();
		port = 8000; // Hvilken port benyttes

		/*
		 * Tilføj de vægtes IP-adresser der skal tjekkes.
		 */
		ip.add("169.254.2.2");
		ip.add("169.254.2.3");
	}

	@Override
	public void run() {
		try {
			while (true){
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
					InetAddress address = InetAddress.getByName(ip.get(i));

					/*
					 *  Tjek om der kan oprettes forbindelse til IP på port 7 (echo). 
					 *  I så fald oprettes en afvejningsprocedure på vægten.
					 */
					if (address.isReachable(500)){ // 500 = 0,5 sek
						IProcedure menu = new Procedure();
						ITransmitter trans = new Transmitter();
						IControllerDAO dao = new ControllerDAO();
						IProcedureController ase = new ProcedureController(menu, dao , ip.get(i), port, trans);
						Thread t = new Thread((Runnable) ase);
						t.start();
						active.add(t);
						activeIp.add(ip.get(i));
						System.out.println("Afvejningsprocedure startet på ip: " + ip.get(i));
						ip.remove(i); 
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
