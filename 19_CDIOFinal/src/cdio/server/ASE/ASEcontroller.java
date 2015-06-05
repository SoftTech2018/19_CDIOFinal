package cdio.server.ASE;

import java.io.FileNotFoundException;

import cdio.server.DAL.DALException;
import cdio.server.DAL.DAO;
import cdio.server.DAL.IDAO;


public class ASEcontroller {

	public void runASE(){
		int port;
		String host;
		
//		if (args.length == 2){
//			port = Integer.parseInt(args[1]);
//			host = args[0];
//		}
//		else {
			port = 8000;
			host = "localhost";			
//		}
		
		IProcedure menu = new Procedure();
		
		try {
			IDAO dao = new DAO();
			ITransmitter trans = new Transmitter();
			IProcedureController menuCon = new ProcedureController(menu,dao, host, port, trans);
			
		} catch (FileNotFoundException | DALException e) {
			System.out.println("Noget i filbehandlingen gik grueligt galt :-( Kontakt udvikleren.");
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
	}

