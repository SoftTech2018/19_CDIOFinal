package cdio.server.DAL.dao;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.idao.IReceptKompDAO;
import cdio.shared.DALException;
import cdio.shared.ReceptKompDTO;

public class ReceptKompDAO implements IReceptKompDAO {
	
	private TextReader txt;
	
	public ReceptKompDAO(TextReader txt) throws FileNotFoundException{
		this.txt = txt;
	}

	@Override
	public ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException {
		ResultSet rs = Connector.doQuery(txt.getReceptKomp(receptId, raavareId));		
	    try {
	    	if (!rs.first()) throw new DALException("ReceptKomponent " + receptId + ", " + raavareId + " findes ikke");
	    	return new ReceptKompDTO (rs.getInt("recept_id"), rs.getInt("raavare_id"), rs.getDouble("nom_netto"),rs.getDouble("tolerance"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		ResultSet rs = Connector.doQuery(txt.getReceptKompList(receptId));
		try {
			while (rs.next()) {
				list.add(new ReceptKompDTO(rs.getInt("recept_id"), rs.getInt("raavare_id"), rs.getDouble("nom_netto"),rs.getDouble("tolerance")));
			}
		} catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList() throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(20));
		try {
			while (rs.next()) {
				list.add(new ReceptKompDTO(rs.getInt("recept_id"), rs.getInt("raavare_id"), rs.getDouble("nom_netto"),rs.getDouble("tolerance")));
			}
		} catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException {
		try{
		Connector.doUpdate(txt.createReceptKomp(receptkomponent));
		} catch (DALException e){ throw new DALException("Receptkomponent er allerede oprettet");}
		}

	@Override
	public void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException {
		Connector.doUpdate(txt.updateReceptKomp(receptkomponent));
	}

}
