package cdio.server;

import cdio.client.Service;
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
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
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
		String password = escapeHtml(user.getPassword());
		UserDTO db_user = new UserDTO("1", "test"); //dal.getUser(userID);
		db_user.setAdmin(true); // TESTKODE SKAL SLETTES
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
//			UserDTO user = dal.getUser(th.getUserID(token));
//			if (user.isAdmin())
//				return "ADMIN";
//			if (user.isFarmaceut())
//				return "FARMACEUT";
//			if (user.isVaerkfoerer())
//				return "VAERKFOERER";
////			if (user.isOperatoer())
////				return "OPERATOER";
//			throw new Exception("Bruger har ingen adgang.");
			return "ADMIN"; // TESTKODE SKAL SLETTES
		}
		throw new Exception("Adgang nægtet.");
	}
}
