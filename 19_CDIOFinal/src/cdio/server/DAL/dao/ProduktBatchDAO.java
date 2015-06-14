package cdio.server.DAL.dao;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.idao.IProduktBatchDAO;
import cdio.shared.DALException;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;

public class ProduktBatchDAO implements IProduktBatchDAO {

	private TextReader txt;

	public ProduktBatchDAO(TextReader txt) throws FileNotFoundException{
		this.txt = txt;
	}

	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws DALException {
		ResultSet rs = Connector.doQuery(txt.getProductBatch(pbId));		
		try {
			if (!rs.first()) throw new DALException("Produktbatch " + pbId + " findes ikke");
			return new ProduktBatchDTO (rs.getInt("pb_id"), rs.getInt("status"), rs.getInt("recept_id"), rs.getString("dato"));
		}
		catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		List<ProduktBatchDTO> list = new ArrayList<ProduktBatchDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(6));
		try {
			while (rs.next()) {
				list.add(new ProduktBatchDTO(rs.getInt("pb_id"), rs.getInt("status"), rs.getInt("recept_id"), rs.getString("dato")));
			}
		} catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		Connector.doUpdate(txt.createProductBatch(produktbatch));
	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		Connector.doUpdate(txt.updateProduktBatch(produktbatch));
	}

	@Override
	public void updateStatus(int pbID, int status) throws DALException {
		Connector.doUpdate(txt.updatePbStatus(pbID, status));
	}

	@Override
	public List<PbViewDTO> getPbViewList(int pb_id) throws DALException{
		ProduktBatchDTO pb = getProduktBatch(pb_id);
		List<PbViewDTO> list = new ArrayList<PbViewDTO>();
		try {
		
		if (pb.getStatus() == 0){
			// Hvis produktbatchkomponent er tom
			ResultSet rs = Connector.doQuery(txt.getPbView2(pb_id));
			while (rs.next()) {
				String ini =  "";
				String terminal = "";
				list.add(new PbViewDTO(rs.getString("raavare_navn"), rs.getInt("raavare_id"),
						ini, rs.getInt("rb_id"), rs.getDouble("nom_netto"), 
						rs.getDouble("tara"), rs.getDouble("netto"), rs.getDouble("tolerance"),
						terminal));
			}
		} else {
			// Hvis produktbatchkomponent ikke er tom
			ResultSet rs = Connector.doQuery(txt.getPbView(pb_id));
			while (rs.next()) {
				String ini = rs.getString("ini");
				if (ini == null)
					ini = "";
				String terminal = rs.getString("terminal");
				if (terminal == null)
					terminal = "";
				list.add(new PbViewDTO(rs.getString("raavare_navn"), rs.getInt("raavare_id"),
						ini, rs.getInt("rb_id"), rs.getDouble("nom_netto"), 
						rs.getDouble("tara"), rs.getDouble("netto"), rs.getDouble("tolerance"),
						terminal));
			}
		}
		
		} catch (SQLException e) { throw new DALException(e); }
		return list;
	}

	@Override
	public int getLatestPbId() throws DALException{
		ResultSet rs = Connector.doQuery("SELECT LAST_INSERT_ID();");
		int pb_id = -1;
		try {
			if (rs.next())
				pb_id = rs.getInt("LAST_INSERT_ID()");				
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return pb_id;
	}

	public void checkReceptID(int id) throws DALException{
		Connector.doUpdate(txt.deleteRecept(id));
	}
	
	public void deleteProduktBatch(int id) throws DALException{
		Connector.doUpdate(txt.deleteProduktBatch(id));
	}
}
