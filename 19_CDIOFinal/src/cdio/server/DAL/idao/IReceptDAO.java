package cdio.server.DAL.idao;

import java.util.List;

import cdio.server.DAL.DALException;
import cdio.server.DAL.dto.ReceptDTO;


public interface IReceptDAO {
	ReceptDTO getRecept(int receptId) throws DALException;
	List<ReceptDTO> getReceptList() throws DALException;
	void createRecept(ReceptDTO recept) throws DALException;
	void updateRecept(ReceptDTO recept) throws DALException;
}
