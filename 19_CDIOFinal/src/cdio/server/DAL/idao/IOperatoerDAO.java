package cdio.server.DAL.idao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cdio.server.DAL.DALException;
import cdio.shared.UserDTO;

public interface IOperatoerDAO {
	public UserDTO getOperatoer(int oprId) throws DALException;
	public List<UserDTO> getOperatoerList() throws DALException;
	public List<UserDTO> getListViewOpr() throws DALException;
	void createOperatoer(UserDTO opr) throws DALException;
	void updateOperatoer(UserDTO opr) throws DALException;
	public ResultSet getView() throws DALException;
	public void callProcedure() throws DALException;
	public void setFunction() throws DALException;
	public String getFunction(int id) throws DALException, SQLException;
	public void dropAll() throws DALException;
	
}
