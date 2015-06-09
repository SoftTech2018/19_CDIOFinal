package cdio.server.DAL.dao;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.Connector;
import cdio.server.DAL.DALException;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.idao.IOperatoerDAO;
import cdio.shared.UserDTO;


public class OperatoerDAO implements IOperatoerDAO {
	
	TextReader txt;
	
	public OperatoerDAO(TextReader txt) throws FileNotFoundException, DALException{
		this.txt = txt;
		Connector.doUpdate(txt.getCommand(44)); // Opretter view: view_oprlist
		Connector.doUpdate(txt.getCommand(51)); // Opretter view: view_produktbatchkompinfo
		this.setProcedure();
	}
	
	public UserDTO getOperatoer(int oprId) throws DALException {
		ResultSet rs = Connector.doQuery(txt.getOperatoer(oprId));
	    try {
	    	if (!rs.first()) throw new DALException("Bruger " + oprId + " findes ikke");
	    	return new UserDTO (Integer.toString(rs.getInt("opr_id")), rs.getString("opr_navn"), rs.getString("ini"), rs.getString("cpr"), rs.getString("password"), rs.getBoolean("admin"), rs.getBoolean("farmaceut"), rs.getBoolean("varkforer"), rs.getBoolean("operatoer"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}
	
	public void createOperatoer(UserDTO opr) throws DALException {		
			Connector.doUpdate(txt.createOperatoer(opr));
	}
	
	public void updateOperatoer(UserDTO opr) throws DALException {
		Connector.doUpdate(txt.updateOperatoer(opr));
		Connector.doUpdate(txt.updateOprRolle(opr));
	}
	
	public List<UserDTO> getOperatoerList() throws DALException {
		List<UserDTO> list = new ArrayList<UserDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(4));
		try
		{
			while (rs.next()) 
			{
				list.add(new UserDTO(Integer.toString(rs.getInt("opr_id")), rs.getString("opr_navn"), rs.getString("ini"), rs.getString("cpr"), rs.getString("password"), rs.getBoolean("admin"), rs.getBoolean("farmaceut"), rs.getBoolean("varkforer"), rs.getBoolean("operatoer")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}
	
	public List<UserDTO> getListViewOpr() throws DALException {
		List<UserDTO> list = new ArrayList<UserDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(43));
		try
		{
			while (rs.next()) 
			{
				list.add(new UserDTO(Integer.toString(rs.getInt("opr_id")), rs.getString("opr_navn"), rs.getString("ini"), null, null, rs.getBoolean("admin"), rs.getBoolean("farmaceut"), rs.getBoolean("varkforer"), rs.getBoolean("operatoer")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}
	
	public ResultSet getView() throws DALException {
		return Connector.doQuery(txt.getCommand(49));
		
	}
	
	public void setProcedure() throws DALException{
		Connector.doUpdate(txt.getCommand(45));
		Connector.doUpdate(txt.getCommand(46));
	}
	
	public void callProcedure() throws DALException{
		Connector.doUpdate(txt.getCommand(47));
	}
	
	public void setFunction() throws DALException{
		Connector.doUpdate(txt.getCommand(48));
	}
	
	public String getFunction(int id) throws DALException, SQLException{
		ResultSet temp = Connector.doQuery(txt.getFunction(id));
		temp.next();
		return temp.getString(1);
	}
	
	public void dropAll() throws DALException {
		Connector.doUpdate(txt.getCommand(37));
		Connector.doUpdate(txt.getCommand(38));
		Connector.doUpdate(txt.getCommand(39));
		Connector.doUpdate(txt.getCommand(40));
	}
		
}
	
