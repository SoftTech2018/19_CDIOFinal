package cdio.server.DAL.idao;

import java.util.List;

import cdio.shared.DALException;
import cdio.shared.ReceptDTO;


public interface IReceptDAO {
	ReceptDTO getRecept(int receptId) throws DALException;
	List<ReceptDTO> getReceptList() throws DALException;
	void createRecept(ReceptDTO recept) throws DALException;
	void updateRecept(ReceptDTO recept) throws DALException;
}
