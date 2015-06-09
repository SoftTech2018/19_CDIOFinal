package cdio.client;

import java.util.List;

import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareDTO;
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

	void updateUser(String token, UserDTO user, AsyncCallback<UserDTO> asyncCallback);
	
	void getRaavareList(String token, AsyncCallback<List<RaavareDTO>> asyncCallback);

	void createUser(String token, UserDTO user,	AsyncCallback<UserDTO> asyncCallback);
	
	void getPBList(String token, AsyncCallback<List<ProduktBatchDTO>> asyncCallback);

	void getPBKList(String token,AsyncCallback<List<ProduktBatchKompDTO>> asyncCallback);
	
}
