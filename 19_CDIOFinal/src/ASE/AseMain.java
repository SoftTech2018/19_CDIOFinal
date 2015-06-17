package ASE;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import cdio.server.DAL.Connector;
import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;

public class AseMain {

	public static void main(String[] args) {
		runASE();
//		Thread t = new Thread((Runnable) new ServerASE());
//		t.start();

	}
	
	/**
	 * Starter ASE-programmet på en defineret vægt.
	 */
	public static void runASE(){
		int port;
		String host;

		//		if (args.length == 2){
		//			port = Integer.parseInt(args[1]);
		//			host = args[0];
		//		}
		//		else {
		port = 8000;
		host = "169.254.2.3";
//		host = "localhost";
		//		}

		IProcedure menu = new Procedure();
		try {
			ITransmitter trans = new Transmitter();
			IControllerDAO dao = new ControllerDAO(0);
			IProcedureController menuCon = new ProcedureController(menu, dao, host, port, trans);
			Thread menuThread = new Thread((Runnable) menuCon);
			menuThread.start();

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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
