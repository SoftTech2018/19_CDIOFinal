package cdio.server.DAL.idao;

import java.util.List;

import cdio.shared.DALException;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;

public interface IProduktBatchDAO {
	ProduktBatchDTO getProduktBatch(int pbId) throws DALException;
	List<ProduktBatchDTO> getProduktBatchList() throws DALException;
	void createProduktBatch(ProduktBatchDTO produktbatch) throws DALException;
	void updateProduktBatch(ProduktBatchDTO produktbatch) throws DALException;
	public void updateStatus(int pbID, int status) throws DALException;
	List<PbViewDTO> getPbViewList(int pb_id) throws DALException;
	int getLatestPbId() throws DALException;
	public void checkReceptID(int id) throws DALException;
}