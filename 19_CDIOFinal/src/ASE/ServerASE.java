package ASE;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;

public class ServerASE implements Runnable{

	private List<String> ipList;
	private List<Thread> active;
	private List<String> activeIp;
	private int port;

	public ServerASE(){
		ipList = new ArrayList<String>();
		active = new ArrayList<Thread>();
		activeIp = new ArrayList<String>();
		port = 8000; // Hvilken port benyttes til afvejningsproceduren
		
		/*
		 * Tilføj de vægtes IP-adresser der skal tjekkes.
		 */
		ipList.add("169.254.2.2");
		ipList.add("169.254.2.3");
		ipList.add("localHost");
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
						ipList.add(activeIp.get(p)); 
						active.remove(p);
						activeIp.remove(p);
					}
				}

				for (int i=0; i<ipList.size(); i++){
					/*
					 *  Tjek om der kan oprettes forbindelse til IP på port 7 (echo). 
					 *  I så fald oprettes en afvejningsprocedure på vægten.
					 */
					Socket socket = null;
					try {
						socket = new Socket();
						socket.connect(new InetSocketAddress(ipList.get(i), port), 1000);  // 1000 = 1 sek til at connecte
					} catch (SocketTimeoutException | ConnectException | NoRouteToHostException e){
						System.out.println("Kunne ikke forbinde til: " + ipList.get(i));
					}  
					if (socket.isConnected()){
						IProcedure menu = new Procedure();
						ITransmitter trans = new Transmitter();
						IControllerDAO dao = new ControllerDAO(0);

						IProcedureController ase = new ProcedureController(socket, menu, dao, trans);
						Thread t = new Thread((Runnable) ase);
						t.start();

						active.add(t);
						activeIp.add(ipList.get(i));
						System.out.println("Afvejningsprocedure startet på ip: " + ipList.get(i));
						ipList.remove(i); 
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