package cdio.server.DAL;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import connector.Connector;
import daointerfaces.DALException;
import daointerfaces.IOperatoerDAO;
import dto.OperatoerDTO;

public class OperatoerDAO implements IOperatoerDAO {
	
	TextReader txt;
	
	public OperatoerDAO() throws FileNotFoundException, DALException{
		txt = new TextReader();
		Connector.doUpdate("CREATE TRIGGER oprTrig BEFORE INSERT ON operatoer FOR EACH ROW SET new.ini = 'trigger aktiveret!'");
		this.setProcedure();
	}
	
	public OperatoerDTO getOperatoer(int oprId) throws DALException {
		ResultSet rs = Connector.doQuery(txt.getOperatoer(oprId));
	    try {
	    	if (!rs.first()) throw new DALException("Operatoeren " + oprId + " findes ikke");
	    	return new OperatoerDTO (rs.getInt("opr_id"), rs.getString("opr_navn"), rs.getString("ini"), rs.getString("cpr"), rs.getString("password"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}
	
	public void createOperatoer(OperatoerDTO opr) throws DALException {		
			Connector.doUpdate(txt.createOperatoer(opr));
	}
	
	public void updateOperatoer(OperatoerDTO opr) throws DALException {
		Connector.doUpdate(txt.updateOperatoer(opr));
	}
	
	public List<OperatoerDTO> getOperatoerList() throws DALException {
		List<OperatoerDTO> list = new ArrayList<OperatoerDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(4));
		try
		{
			while (rs.next()) 
			{
				list.add(new OperatoerDTO(rs.getInt("opr_id"), rs.getString("opr_navn"), rs.getString("ini"), rs.getString("cpr"), rs.getString("password")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}
	
	public ResultSet getView() throws DALException {
		return Connector.doQuery("select * from oprView");
		
	}
	
	public void setProcedure() throws DALException{
		Connector.doUpdate("CREATE PROCEDURE setView() begin CREATE VIEW oprView AS SELECT opr_id, opr_navn, ini FROM operatoer; END;");
	}
	
	public void callProcedure() throws DALException{
		Connector.doUpdate("call setView()");
	}
	
	public void setFunction() throws DALException{
		Connector.doUpdate("CREATE FUNCTION oprID(oID INT) RETURNS VARCHAR(20) BEGIN DECLARE navn VARCHAR(20); SELECT opr_navn INTO navn FROM operatoer WHERE opr_id = oID; RETURN navn; END;");
	}
	
	public String getFunction(int id) throws DALException, SQLException{
		ResultSet temp = Connector.doQuery("SELECT oprID("+id+")");
		temp.next();
		return temp.getString(1);
	}
	
	public void dropAll() throws DALException {
		Connector.doUpdate("drop view oprView");
		Connector.doUpdate("drop trigger oprTrig");
		Connector.doUpdate("drop procedure setView");
		Connector.doUpdate("drop function oprID");
	}	
		
}
	
