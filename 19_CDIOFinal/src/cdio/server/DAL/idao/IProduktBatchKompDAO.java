package cdio.server.DAL.idao;

import java.util.List;

import cdio.shared.DALException;
import cdio.shared.ProduktBatchKompDTO;

public interface IProduktBatchKompDAO {
	ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws DALException;
	List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws DALException;
	List<ProduktBatchKompDTO> getProduktBatchKompList() throws DALException;
	void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException;
	void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException;	
}

