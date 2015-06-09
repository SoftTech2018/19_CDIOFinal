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
	    	return new ProduktBatchDTO (rs.getInt("pb_id"), rs.getInt("status"), rs.getInt("recept_id"));
	    }
	    catch (SQLException e) {throw new DALException(e); }
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		List<ProduktBatchDTO> list = new ArrayList<ProduktBatchDTO>();
		ResultSet rs = Connector.doQuery(txt.getCommand(6));
		try {
			while (rs.next()) {
				list.add(new ProduktBatchDTO(rs.getInt("pb_id"), rs.getInt("status"), rs.getInt("recept_id")));
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

}
