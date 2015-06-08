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
import cdio.server.DAL.DALException;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.FieldVerifier;
import cdio.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {
	
	private boolean TEST_DELAY = false; // Sæt til TRUE hvis du tester. Simulerer 2 sekunders delay på hvert server svar

	private TokenHandler th;
	
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
			host = "localhost";			
//		}
		
		IProcedure menu = new Procedure();
		
		try {
			Connector con = new Connector();
			IControllerDAO dao = new ControllerDAO();
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
			throw new Exception("Adgang nægtet.");
		
		UserDTO db_user;
//		try {
			db_user = new UserDTO("1", "test"); //dal.getUser(userID);
			db_user.setAdmin(true); // TESTKODE SKAL SLETTES	
//		} catch (DALException e){
//			throw new Exception("Forkert bruger ID.");
//		}
		String token;		
		
		if (db_user.getPassword().equals(password)){
			if (db_user.isAdmin() || db_user.isFarmaceut() || db_user.isVaerkfoerer()){
				token = th.createToken(Integer.toString(db_user.getUserId()));
			} else {
				throw new Exception("Du har ikke adgang til at logge ind.");
			}
//			dal.log(userID, true);
		} else {
//			dal.log(userID, false);
			throw new Exception("Forkert password.");
		}
		return token; 
	}

	@Override
	public String getRole(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);
		
		if (th.validateToken(token) != null){
			return "ADMIN"; // TESTKODE SKAL SLETTES
//			UserDTO user = dal.getUser(th.getUserID(token));
//			if (user.isAdmin())
//				return "ADMIN";
//			if (user.isFarmaceut())
//				return "FARMACEUT";
//			if (user.isVaerkfoerer())
//				return "VAERKFOERER";
////			if (user.isOperatoer()) // Skal en operatør kunne logge ind?
////				return "OPERATOER";
//			throw new Exception("Bruger har ingen adgang.");
		}
		throw new Exception("Adgang nægtet.");
	}

	@Override
	public String getUsername(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);
		
		if (th.validateToken(token) != null){
//			UserDTO user = dal.getUser(Integer.parseInt(th.getUserID(token)));
//			return user.getName();
			return "Jon"; // TESTKODE
		}
		throw new Exception("Adgang nægtet.");
	}

	@Override
	public List<UserDTO> getOprList(String token) throws Exception {
		if (TEST_DELAY)
			Thread.sleep(2000);
		if (th.validateToken(token) != null){
			List<UserDTO> oprList = new ArrayList<UserDTO>();
			oprList.add(new UserDTO("1", "Ebbe B. Berthold", "EBB", "031091-2223", "password", 
					   true, true, true, true));
			oprList.add(new UserDTO("2", "Jon Tvermose Nielsen", "JTN", "031091-2223", "password", 
					   true, true, true, true));
			oprList.add(new UserDTO("3", "Jacob W. Jepsen", "JWJ", "031091-2223", "password", 
					   true, true, true, true));
		
		return oprList;
		}
		throw new Exception("Adgang nægtet");
		
	}

	@Override
	public UserDTO updateUser(String token, UserDTO user) throws Exception {
//		if (th.validateToken(token) != null){
//			// Opdater brugeren i Databasen
//			return user;
//		} else {
			throw new Exception("Adgang nægtet");
//		}
	}
}
