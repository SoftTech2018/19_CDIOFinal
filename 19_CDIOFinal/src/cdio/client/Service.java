package cdio.client;

import java.util.List;

import cdio.shared.DALException;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
import cdio.shared.TokenException;
import cdio.shared.UserDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface Service extends RemoteService {

	String login(UserDTO user) throws TokenException, DALException;
	
	String getRole(String token) throws TokenException, DALException;
	
	String getUsername(String token) throws TokenException, DALException;
	
	List<UserDTO> getOprList(String token) throws TokenException, DALException;
	
	UserDTO updateUser(String token, UserDTO user) throws TokenException, DALException;
	
	List<RaavareDTO> getRaavareList(String token) throws TokenException, DALException;
	
	void createUser(String token, UserDTO user) throws TokenException, DALException;
	
	List<ProduktBatchDTO> getPBList(String token) throws TokenException, DALException;
	
	List<ProduktBatchKompDTO> getPBKList(String token, int pbID) throws TokenException, DALException;
	
	void deleteUser(String token, int userId) throws TokenException, DALException;
	
	public void updateRaavare(String token, RaavareDTO raavare) throws TokenException, DALException;
	
	List<ReceptDTO> getReceptList(String token) throws TokenException, DALException;
	
	void createRecept(String token, ReceptDTO recept) throws TokenException, DALException;
	
	void createRaavare(String token, RaavareDTO raavare) throws TokenException, DALException;
	
	void createRaavareBatch(String token, RaavareBatchDTO raavareBatch) throws TokenException, DALException;
	
	List<PbViewDTO> getPbView(String token, int receptId) throws TokenException, DALException;
	
	ProduktBatchDTO createPB(String token, ProduktBatchDTO pb)throws TokenException, DALException;
	
	List<RaavareBatchDTO> getRaavareBatchList(String token) throws TokenException, DALException;

	void getRaavareID(String token, int raavareid) throws TokenException, DALException;

	void createReceptKomp(String token, ReceptKompDTO receptkomp) throws TokenException, DALException;
	
	void checkReceptID(String token, int id) throws TokenException, DALException;
	
	String refreshToken(String token) throws TokenException, DALException;
	
	List<Integer> getUserCount(String token) throws TokenException, DALException;
	
	void deleteProduktBatch(String token, int id) throws TokenException, DALException;
	
	Integer getUserId(String token) throws TokenException, DALException;
	
	void deleteRaavare(String token, int raavare_id) throws TokenException, DALException;
}
