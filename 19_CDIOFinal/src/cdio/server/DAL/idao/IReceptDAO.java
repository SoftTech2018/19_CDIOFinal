package cdio.server.DAL.idao;

import java.util.List;

import cdio.server.DAL.dto.ReceptDTO;
import cdio.shared.DALException;


public interface IReceptDAO {
	ReceptDTO getRecept(int receptId) throws DALException;
	List<ReceptDTO> getReceptList() throws DALException;
	void createRecept(ReceptDTO recept) throws DALException;
	void updateRecept(ReceptDTO recept) throws DALException;
}
