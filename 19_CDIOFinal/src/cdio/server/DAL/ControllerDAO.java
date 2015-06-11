package cdio.server.DAL;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

import cdio.server.DAL.dao.OperatoerDAO;
import cdio.server.DAL.dao.ProduktBatchDAO;
import cdio.server.DAL.dao.ProduktBatchKompDAO;
import cdio.server.DAL.dao.RaavareBatchDAO;
import cdio.server.DAL.dao.RaavareDAO;
import cdio.server.DAL.dao.ReceptDAO;
import cdio.server.DAL.dao.ReceptKompDAO;
import cdio.server.DAL.dto.ReceptKompDTO;
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

	@Override
	public void deleteUser(int userId) throws DALException {
		UserDTO user = oprDAO.getOperatoer(userId);
		user.setAdmin(false);
		user.setFarmaceut(false);
		user.setVaerkfoerer(false);
		user.setOperatoer(false);
		oprDAO.updateOperatoer(user);
	}
	
	public void updateRaavare(RaavareDTO raavare) throws DALException{
		raavareDAO.updateRaavare(raavare);
	}
	
	public List<ReceptDTO> getReceptList() throws DALException {
		return receptDAO.getReceptList();
	}

	@Override
	public ProduktBatchDTO createPB(ProduktBatchDTO pb) throws DALException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String _day = String.format("%02d", day);
		int month = cal.get(Calendar.MONTH) +1;
		String _month = String.format("%02d", month);
		int year = cal.get(Calendar.YEAR);
		String _year = Integer.toString(year);
		String time = _day+"-"+_month+"-"+_year;
		pb.setDato(time);
		pbDAO.createProduktBatch(pb);
		return pb;
	}
	
	@Override
	public void createRecept(ReceptDTO recept) throws DALException {
		receptDAO.createRecept(recept);
	}

	@Override
	public ProduktBatchDTO getProduktBatch(int pbID) throws DALException {
		return pbDAO.getProduktBatch(pbID);
	}

	@Override
	public boolean getProduktBatchKompListIsEmpty(int pbId)
			throws DALException {
		return pbkompDAO.getProduktBatchKompList(pbId).isEmpty();
	}
	
	public String getReceptName(int pbID) throws DALException{
		return receptDAO.getRecept(pbDAO.getProduktBatch(pbID).getReceptId()).getReceptNavn();
	}

	public List<ReceptKompDTO> setReceptKompListe(int rkList) throws DALException {
		return receptKompDAO.getReceptKompList(rkList);
	}

	@Override
	public String getSpecificRaavare(int raavare) throws DALException {
		return raavareDAO.getRaavare(raavare).getRaavareNavn();
	}

	@Override
	public void createProduktBatchKomp(ProduktBatchKompDTO pbk)
			throws DALException {
		pbkompDAO.createProduktBatchKomp(pbk);
	}
	
	public void updatePbStatus(int pbID, int status) throws DALException{
		pbDAO.updateStatus(pbID, status);
	}
	
	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		return pbDAO.getProduktBatchList();
	}

	@Override
	public List<ProduktBatchKompDTO> getPBKList(int pbID) throws DALException {
		return pbkompDAO.getProduktBatchKompList(pbID);
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList() throws DALException {
		return rbDAO.getRaavareBatchList();
	}

	@Override
	public List<PbViewDTO> getPbViewList(int pb_id) throws DALException {
		return pbDAO.getPbViewList(pb_id);
	}
}
	
