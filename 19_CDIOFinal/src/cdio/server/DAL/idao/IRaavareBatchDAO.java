package cdio.server.DAL.idao;

import java.util.List;

import cdio.server.DAL.dto.RaavareBatchDTO;
import cdio.shared.DALException;

public interface IRaavareBatchDAO {
	RaavareBatchDTO getRaavareBatch(int rbId) throws DALException;
	List<RaavareBatchDTO> getRaavareBatchList() throws DALException;
	List<RaavareBatchDTO> getRaavareBatchList(int raavareId) throws DALException;
	void createRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException;
	void updateRaavareBatch(RaavareBatchDTO raavarebatch) throws DALException;
}

