package cdio.server.DAL.dao;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.idao.IRaavareDAO;
import cdio.shared.DALException;
import cdio.shared.RaavareDTO;

public class RaavareDAO implements IRaavareDAO {
	
private TextReader txt;
	
	public RaavareDAO(TextReader txt) throws FileNotFoundException{
		this.txt = txt;
	}

	@Override
	public RaavareDTO getRaavare(int raavareId) throws DALException {
		ResultSet rs = Connector.doQuery(txt.getRaavare(raavareId));
	    try {
	    	if (!rs.first()) throw new DALException("Raavare " + raavareId + " findes ikke");
	    	return new RaavareDTO(rs.getInt("raavare_id"), rs.getString("raavare_navn"), rs.getString("leverandoer"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<RaavareDTO> getRaavareList() throws DALException {
		List<RaavareDTO> list = new ArrayList<RaavareDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(29));
		try {
			while (rs.next()) {
				list.add(new RaavareDTO(rs.getInt("raavare_id"), rs.getString("raavare_navn"), rs.getString("leverandoer")));
			}
		}
		catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createRaavare(RaavareDTO raavare) throws DALException {
		Connector.doUpdate(txt.createRaavare(raavare));
	}

	@Override
	public void updateRaavare(RaavareDTO raavare) throws DALException {
		Connector.doUpdate(txt.updateRaavare(raavare));
	}
	
	public void deleteRaavare(int id) throws DALException {
		ResultSet rs = Connector.doQuery(txt.deleteRaavare(id));
		try {
			if(!rs.next()){
				Connector.doUpdate(txt.deleteRaavareFinal(id));
			} else {
				throw new DALException("Raavaren bliver brugt i en recept og kan ikke slettes.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
