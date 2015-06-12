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
	
	List<RaavareDTO> getRaavareList(String token) throws Exception;
	
	void createUser(String token, UserDTO user) throws Exception;
	
	List<ProduktBatchDTO> getPBList(String token) throws Exception;
	
	List<ProduktBatchKompDTO> getPBKList(String token, int pbID) throws Exception;
	
	void deleteUser(String token, int userId) throws Exception;
	
	public void updateRaavare(String token, RaavareDTO raavare) throws Exception;
	
	List<ReceptDTO> getReceptList(String token) throws Exception;
	
	void createRecept(String token, ReceptDTO recept) throws Exception;
	
	void createRaavare(String token, RaavareDTO raavare) throws Exception;
	
	List<PbViewDTO> getPbView(String token, int receptId) throws Exception;
	
	ProduktBatchDTO createPB(String token, ProduktBatchDTO pb)throws Exception;
	
	List<RaavareBatchDTO> getRaavareBatchList(String token) throws Exception;

	void getRaavareID(String token, int raavareid) throws Exception;

	void createReceptKomp(String token, ReceptKompDTO receptkomp) throws Exception;
	
	void deleteRecept(String token, int id) throws Exception;
	
	String refreshToken(String token) throws Exception;
	
}
