package cdio.client;

import java.util.List;

import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
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

	void createUser(String token, UserDTO user,	AsyncCallback<Void> asyncCallback);
	
	void getPBList(String token, AsyncCallback<List<ProduktBatchDTO>> asyncCallback);

	void getPBKList(String token,int pdID, AsyncCallback<List<ProduktBatchKompDTO>> asyncCallback);

	void deleteUser(String token, int userId, AsyncCallback<Void> asyncCallback);
	
	void updateRaavare(String token, RaavareDTO raavare, AsyncCallback<Void> asyncCallback);
	
	void getReceptList(String token, AsyncCallback<List<ReceptDTO>> asyncCallback);

	void getRaavareBatchList(String token, AsyncCallback<List<RaavareBatchDTO>> asyncCallback);

	void createPB(String token, ProduktBatchDTO pb,
			AsyncCallback<ProduktBatchDTO> asyncCallback);

	void getPbView(String token, int receptId,
			AsyncCallback<List<PbViewDTO>> asyncCallback);
	
	void createRecept(String token, ReceptDTO recept, AsyncCallback<Void> asyncCallback);

	void getRaavareID(String token,  int raavareid, AsyncCallback<Void> asyncCallback);
	
	void createRaavare(String token, RaavareDTO raavare ,AsyncCallback<Void> asyncCallback);
	
	void createReceptKomp(String token, ReceptKompDTO receptkomp, AsyncCallback<Void> asyncCallback);
	
	void checkReceptID(String token, int id, AsyncCallback<Void> asyncCallback);

	void refreshToken(String token, AsyncCallback<String> asyncCallback);
	
	void getUserCount(String token, AsyncCallback<List<Integer>> asyncCallback);
	
	void deleteProduktBatch(String token, int id, AsyncCallback<Void> asyncCallback);
	
}
