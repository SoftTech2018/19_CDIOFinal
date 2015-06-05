package cdio.server.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IDAO {
	DTO getOperatoer(int oprId) throws DALException;
	List<DTO> getOperatoerList() throws DALException;
	void createOperatoer(DTO opr) throws DALException;
	void updateOperatoer(DTO opr) throws DALException;
	public ResultSet getView() throws DALException;
	public void callProcedure() throws DALException;
	public void setFunction() throws DALException;
	public String getFunction(int id) throws DALException, SQLException;
	public void dropAll() throws DALException;	
}
