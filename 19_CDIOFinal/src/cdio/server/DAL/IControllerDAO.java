package cdio.server.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cdio.server.DAL.idao.IOperatoerDAO;
import cdio.server.DAL.idao.IProduktBatchDAO;
import cdio.server.DAL.idao.IProduktBatchKompDAO;
import cdio.server.DAL.idao.IRaavareBatchDAO;
import cdio.server.DAL.idao.IRaavareDAO;
import cdio.server.DAL.idao.IReceptDAO;
import cdio.server.DAL.idao.IReceptKompDAO;

public interface IControllerDAO {
	
	public IOperatoerDAO getOprDAO();
	
	public IProduktBatchDAO getPbDAO();
		
	public IProduktBatchKompDAO getPbKompDAO();
		
	public IReceptDAO getReceptDAO();
	
	public IReceptKompDAO getReceptKompDAO();
	
	public IRaavareBatchDAO getRbDAO();
	
	public IRaavareDAO getRaavareDAO();
	
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