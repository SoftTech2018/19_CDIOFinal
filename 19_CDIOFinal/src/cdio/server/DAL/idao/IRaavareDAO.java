package cdio.server.DAL.idao;

import java.util.List;

import cdio.server.DAL.DALException;
import cdio.shared.RaavareDTO;

public interface IRaavareDAO {
	RaavareDTO getRaavare(int raavareId) throws DALException;
	List<RaavareDTO> getRaavareList() throws DALException;
	void createRaavare(RaavareDTO raavare) throws DALException;
	void updateRaavare(RaavareDTO raavare) throws DALException;
}
