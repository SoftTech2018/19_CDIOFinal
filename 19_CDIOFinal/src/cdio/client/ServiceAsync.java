package cdio.client;

import java.util.List;

import cdio.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ServiceAsync {
	
	void login(UserDTO user, AsyncCallback<String> callback);

	void getRole(String token, AsyncCallback<String> asyncCallback);

	void getUsername(String token, AsyncCallback<String> asyncCallback);

	void getOprList(String token, AsyncCallback<List<UserDTO>> asyncCallback);
}
