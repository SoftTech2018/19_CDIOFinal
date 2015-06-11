package cdio.server;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.client.Service;
import cdio.server.ASE.IProcedure;
import cdio.server.ASE.IProcedureController;
import cdio.server.ASE.ITransmitter;
import cdio.server.ASE.Procedure;
import cdio.server.ASE.ProcedureController;
import cdio.server.ASE.Transmitter;
import cdio.server.DAL.Connector;
import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;
import cdio.shared.FieldVerifier;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {

	private boolean TEST_DELAY = false; // Sæt til TRUE hvis du tester. Simulerer 2 sekunders delay på hvert server svar

	private TokenHandler th;
	private IControllerDAO dao;

	public ServiceImpl(){
		th = new TokenHandler();
		runASE();
	}

	public void runASE(){
		int port;
		String host;

		//		if (args.length == 2){
		//			port = Integer.parseInt(args[1]);
		//			host = args[0];
		//		}
		//		else {
		port = 8000;
//		host = "169.254.2.3";
		host = "localhost";
		//		}

		IProcedure menu = new Procedure();

		try {
			Connector con = new Connector();
			dao = new ControllerDAO();
			ITransmitter trans = new Transmitter();
			IProcedureController menuCon = new ProcedureController(menu,dao, host, port, trans);
			Thread menuThread = new Thread((Runnable) menuCon);
			menuThread.start();

		} catch (FileNotFoundException e) {
			System.out.println("Noget i filbehandlingen gik grueligt galt :-( Kontakt udvikleren.");
			e.printStackTrace();
		} catch (DALException e){
			System.out.println("DALException");
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

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public String login(UserDTO user) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);

		String userID = escapeHtml(Integer.toString(user.getUserId()));
		String password = escapeHtml(user.getPassword());

		// Inputvalidering på serveren
		if (!FieldVerifier.isValidUserId(userID) || !FieldVerifier.isValidPassword(password))
			throw new Exception("Ugyldigt bruger id og/eller password.");

		UserDTO db_user;
		try {
			db_user = dao.getUser(Integer.parseInt(userID));
		} catch (DALException e){
			throw new Exception("Forkert bruger ID.");
		}
		String token;		

		if (db_user.getPassword().equals(password)){
			if (db_user.isAdmin() || db_user.isFarmaceut() || db_user.isVaerkfoerer()){
				token = th.createToken(Integer.toString(db_user.getUserId()));
			} else {
				throw new Exception("Du har ikke adgang til at logge ind.");
			}
		} else {
			throw new Exception("Forkert password.");
		}
		return token; 
	}

	@Override
	public String getRole(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);

		if (th.validateToken(token) != null){
			UserDTO user = dao.getUser(Integer.parseInt(th.getUserID(token)));
			if (user.isAdmin())
				return "ADMIN";
			if (user.isFarmaceut())
				return "FARMACEUT";
			if (user.isVaerkfoerer())
				return "VAERKFOERER";
			//			if (user.isOperatoer()) // Skal en operatør kunne logge ind?
			//				return "OPERATOER";
			throw new Exception("Bruger har ingen adgang.");
		}
		throw new Exception("Adgang nægtet.");
	}

	@Override
	public String getUsername(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);

		if (th.validateToken(token) != null){
			UserDTO user = dao.getUser(Integer.parseInt(th.getUserID(token)));
			return user.getName();
		}
		throw new Exception("Adgang nægtet.");
	}

	@Override
	public List<UserDTO> getOprList(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);
		if (th.validateToken(token) != null){
			return dao.getOprList();
		}
		throw new Exception("Adgang nægtet");

	}

	@Override
	public UserDTO updateUser(String token, UserDTO user) throws Exception {
		if (th.validateToken(token) != null)
			return dao.updateUser(user);
		else 
			throw new Exception("Adgang nægtet");
	}

	public List<RaavareDTO> getRaavareList(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);
		if (th.validateToken(token) != null){
			return dao.getRaavareList();
		}
		throw new Exception("Adgang nægtet");

	}

	@Override
	public void createUser(String token, UserDTO user) throws Exception {
		if (th.validateToken(token) != null){
			dao.createUser(user);						
		}
		else 
			throw new Exception("Adgang nægtet");
	}

	public List<ProduktBatchDTO> getPBList(String token) throws Exception {
		if (th.validateToken(token) != null){
			return dao.getProduktBatchList();				
		}
		else 
			throw new Exception("Adgang nægtet");
	}

	@Override
	public List<ProduktBatchKompDTO> getPBKList(String token, int pbID) throws Exception {
		if (th.validateToken(token) != null){
			return dao.getPBKList(pbID);						
		}
		else 
			throw new Exception("Adgang nægtet");
	}

	@Override
	public void deleteUser(String token, int userId) throws Exception {
		if (th.validateToken(token) != null){
			dao.deleteUser(userId);					
		}
		else 
			throw new Exception("Adgang nægtet");
		
	}
	
	public void updateRaavare(String token, RaavareDTO raavare) throws Exception{
		if (th.validateToken(token) != null){
			dao.updateRaavare(raavare);
		} else {
			throw new Exception("Adgang nægtet");
		}
	}
	
	
	public List<ReceptDTO> getReceptList(String token) throws Exception{
		if(TEST_DELAY)
			Thread.sleep(2000);
		if(th.validateToken(token) != null){
		 return dao.getReceptList();
		}
		throw new Exception("Adgang nægtet");
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList(String token) throws Exception {
		if(TEST_DELAY)
			Thread.sleep(2000);
		if(th.validateToken(token) != null){
		 return dao.getRaavareBatchList();
		}
		throw new Exception("Adgang nægtet");
	}

	@Override
	public ProduktBatchDTO createPB(String token, ProduktBatchDTO pb)throws Exception {
		return dao.createPB(pb);
	}

	@Override
	public List<PbViewDTO> getPbView(String token, int pb_id) throws Exception {
		Thread.sleep(500);
		List<PbViewDTO> list = dao.getPbViewList(pb_id);
		return list;
	}
	
	public void createRecept(String token, ReceptDTO recept) throws Exception {
		if (th.validateToken(token) != null){
			try{
			dao.createRecept(recept);}
		catch(Exception DALException){
			throw new Exception("Receptnummer findes allerede!");
			}
		
				
			}
		}
	}


