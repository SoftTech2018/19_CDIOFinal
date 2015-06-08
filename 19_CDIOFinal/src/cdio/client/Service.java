package cdio.client;

import java.util.List;

import cdio.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface Service extends RemoteService {

	String login(UserDTO user) throws Exception;
	
	String getRole(String token) throws Exception;
	
	String getUsername(String token) throws Exception;
	
	List<UserDTO> getOprList(String token) throws Exception;
	
	UserDTO updateUser(String token, UserDTO user) throws Exception;
}
