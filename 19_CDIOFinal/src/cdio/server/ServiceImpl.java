package cdio.server;

import cdio.client.Service;
import cdio.server.DAL.DALException;
import cdio.shared.FieldVerifier;
import cdio.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {
	
	private TokenHandler th;
	
	public ServiceImpl(){
		th = new TokenHandler();
	}

	public String greetServer(String input) throws IllegalArgumentException {
		return input;
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
		if (th.validateToken(token) != null){
//			UserDTO user = dal.getUser(Integer.parseInt(th.getUserID(token)));
//			return user.getName();
			return "Jon"; // TESTKODE
		}
		throw new Exception("Adgang nægtet.");
	}
}
