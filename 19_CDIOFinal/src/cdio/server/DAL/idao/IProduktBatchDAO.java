package cdio.server.DAL.idao;

import java.util.List;

import cdio.shared.DALException;
import cdio.shared.ProduktBatchDTO;

public interface IProduktBatchDAO {
	ProduktBatchDTO getProduktBatch(int pbId) throws DALException;
	List<ProduktBatchDTO> getProduktBatchList() throws DALException;
	void createProduktBatch(ProduktBatchDTO produktbatch) throws DALException;
	void updateProduktBatch(ProduktBatchDTO produktbatch) throws DALException;
	public void updateStatus(int pbID, int status) throws DALException;
}