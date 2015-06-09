package cdio.server.DAL;

import java.io.FileNotFoundException;
import java.util.List;

import cdio.server.DAL.dao.OperatoerDAO;
import cdio.server.DAL.dao.ProduktBatchDAO;
import cdio.server.DAL.dao.ProduktBatchKompDAO;
import cdio.server.DAL.dao.RaavareBatchDAO;
import cdio.server.DAL.dao.RaavareDAO;
import cdio.server.DAL.dao.ReceptDAO;
import cdio.server.DAL.dao.ReceptKompDAO;
import cdio.server.DAL.idao.IOperatoerDAO;
import cdio.server.DAL.idao.IProduktBatchDAO;
import cdio.server.DAL.idao.IProduktBatchKompDAO;
import cdio.server.DAL.idao.IRaavareBatchDAO;
import cdio.server.DAL.idao.IRaavareDAO;
import cdio.server.DAL.idao.IReceptDAO;
import cdio.server.DAL.idao.IReceptKompDAO;
import cdio.shared.RaavareDTO;
import cdio.shared.UserDTO;


public class ControllerDAO implements IControllerDAO {
	
	IOperatoerDAO oprDAO;
	IProduktBatchDAO pbDAO;
	IProduktBatchKompDAO pbkompDAO;
	IReceptDAO receptDAO;
	IReceptKompDAO receptKompDAO;
	IRaavareBatchDAO rbDAO;
	IRaavareDAO raavareDAO;
	TextReader txt;
	
	public ControllerDAO() throws FileNotFoundException, DALException{
		txt = new TextReader();
		oprDAO = new OperatoerDAO(txt);
		pbDAO = new ProduktBatchDAO(txt);
		pbkompDAO = new ProduktBatchKompDAO(txt);
		receptDAO = new ReceptDAO(txt);
		receptKompDAO = new ReceptKompDAO(txt);
		rbDAO = new RaavareBatchDAO(txt);
		raavareDAO = new RaavareDAO(txt);
	}
	
	public IOperatoerDAO getOprDAO(){
		return oprDAO;
	}
	
	public IProduktBatchDAO getPbDAO(){
		return pbDAO;
	}
		
	public IProduktBatchKompDAO getPbKompDAO(){
		return pbkompDAO;
	}
		
	public IReceptDAO getReceptDAO(){
		return receptDAO;
	}
	
	public IReceptKompDAO getReceptKompDAO(){
		return receptKompDAO;
	}
	
	public IRaavareBatchDAO getRbDAO(){
		return rbDAO;
	}
	
	public IRaavareDAO getRaavareDAO(){
		return raavareDAO;
	}

	@Override
	public UserDTO getUser(int id) throws DALException {
		return oprDAO.getOperatoer(id);
	}

	@Override
	public List<UserDTO> getOprList() throws DALException {
		return oprDAO.getOperatoerList();
	}

	@Override
	public UserDTO updateUser(UserDTO user) throws DALException {
		oprDAO.updateOperatoer(user);
		return oprDAO.getOperatoer(user.getUserId());
	}

	@Override
	public void createUser(UserDTO user) throws DALException {
		oprDAO.createOperatoer(user);
	}
	
	public List<RaavareDTO> getRaavareList() throws DALException {
		return raavareDAO.getRaavareList();
	}
	
}
	
