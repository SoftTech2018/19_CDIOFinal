package cdio.server.DAL;

import java.util.List;

import cdio.server.DAL.idao.IOperatoerDAO;
import cdio.server.DAL.idao.IProduktBatchDAO;
import cdio.server.DAL.idao.IProduktBatchKompDAO;
import cdio.server.DAL.idao.IRaavareBatchDAO;
import cdio.server.DAL.idao.IRaavareDAO;
import cdio.server.DAL.idao.IReceptDAO;
import cdio.server.DAL.idao.IReceptKompDAO;
import cdio.shared.DALException;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
import cdio.shared.UserDTO;

public interface IControllerDAO {
	
	public IOperatoerDAO getOprDAO();
	
	public IProduktBatchDAO getPbDAO();
		
	public IProduktBatchKompDAO getPbKompDAO();
		
	public IReceptDAO getReceptDAO();
	
	public IReceptKompDAO getReceptKompDAO();
	
	public IRaavareBatchDAO getRbDAO();
	
	public IRaavareDAO getRaavareDAO();

	public UserDTO getUser(int parseInt) throws DALException;

	public List<UserDTO> getOprList() throws DALException;

	public UserDTO updateUser(UserDTO user) throws DALException;

	public void createUser(UserDTO user) throws DALException;
	
	public List<RaavareDTO> getRaavareList() throws DALException;

	public void deleteUser(int userId) throws DALException;
	
	public void updateRaavare(RaavareDTO raavare) throws DALException;
	
	public List<ReceptDTO> getReceptList() throws DALException;

	public ProduktBatchDTO createPB(ProduktBatchDTO pb) throws DALException;
	
	void createRecept(ReceptDTO recept) throws DALException;
	
	ProduktBatchDTO getProduktBatch(int pbID) throws DALException;
	
	boolean getProduktBatchKompListIsEmpty(int pbId) throws DALException;
	
	public String getReceptName(int pbID) throws DALException;
	
	public List<ReceptKompDTO> getReceptKompListe(int rkList) throws DALException;
	
	public String getSpecificRaavare(int raavare) throws DALException;
	
	public void createProduktBatchKomp(ProduktBatchKompDTO pbk) throws DALException;
	
	public void updatePbStatus(int pbID, int status) throws DALException;
	
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException;
	
	public List<ProduktBatchKompDTO> getPBKList(int pbID) throws DALException;

	public List<RaavareBatchDTO> getRaavareBatchList() throws DALException;
	
	public List<PbViewDTO> getPbViewList(int pb_id) throws DALException;
	
	public void createRaavare(RaavareDTO raavare) throws DALException;
	
	public void createRaavareBatch(RaavareBatchDTO raavareBatch) throws DALException;

	public int getRaavareID(int raavareid) throws DALException;
	
	public void createReceptKomp(ReceptKompDTO receptkomp) throws DALException;
	
	public void checkReceptID(int id) throws DALException;
	
	public List<Integer> getUserCount() throws DALException;
	
	public void deleteProduktBatch(int id) throws DALException;
	
//	DTO getOperatoer(int oprId) throws DALException;
//	List<DTO> getOperatoerList() throws DALException;
//	void createOperatoer(DTO opr) throws DALException;
//	void updateOperatoer(DTO opr) throws DALException;
//	public ResultSet getView() throws DALException;
//	public void callProcedure() throws DALException;
//	public void setFunction() throws DALException;
//	public String getFunction(int id) throws DALException, SQLException;
//	public void dropAll() throws DALException;	
}
