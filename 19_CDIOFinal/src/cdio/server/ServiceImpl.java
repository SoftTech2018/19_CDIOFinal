package cdio.server;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import cdio.client.Service;
import cdio.server.DAL.ControllerDAO;
import cdio.server.DAL.IControllerDAO;
import cdio.shared.DALException;
import cdio.shared.FieldVerifier;
import cdio.shared.PbViewDTO;
import cdio.shared.ProduktBatchDTO;
import cdio.shared.ProduktBatchKompDTO;
import cdio.shared.RaavareBatchDTO;
import cdio.shared.RaavareDTO;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;
import cdio.shared.TokenException;
import cdio.shared.UserDTO;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {

	private boolean TEST_DELAY = false; // Sæt til TRUE hvis du tester. Simulerer 2 sekunders delay på hvert server svar

	private TokenHandler th;
	private IControllerDAO dao;

	public ServiceImpl() throws FileNotFoundException, DALException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		th = new TokenHandler();
		dao = new ControllerDAO();
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 * @throws DALException 
	 */
	private String escapeHtml(String html) throws DALException {
		if (html == null)
			return null;
		html = html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		if (!FieldVerifier.illigalChars(html))
			throw new DALException("Ulovligt input");
		return html;
	}

	@Override
	public String login(UserDTO user) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {}

		// Sikring mod SQL-injection
		String userID = escapeHtml(Integer.toString(user.getUserId()));
		String password = escapeHtml(user.getPassword());

		// Inputvalidering på serveren
		if (!FieldVerifier.isValidUserId(userID) || !FieldVerifier.isValidPassword(password))
			throw new TokenException("Ugyldigt bruger id og/eller password.");

		UserDTO db_user = dao.getUser(Integer.parseInt(userID));
		String token;		

		if (db_user.getPassword().equals(password)){
			if (db_user.isAdmin() || db_user.isFarmaceut() || db_user.isVaerkfoerer()){
				token = th.createToken(Integer.toString(db_user.getUserId()));
			} else {
				throw new TokenException("Du har ikke adgang til at logge ind.");
			}
		} else {
			throw new TokenException("Forkert password.");
		}
		return token; 
	}

	@Override
	public String getRole(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {}

		if (th.validateToken(token) != null){
			UserDTO user;
			try {
				user = dao.getUser(Integer.parseInt(th.getUserID(token)));				
			} catch (NumberFormatException e) {
				throw new TokenException("Ugyldigt bruger-id.");
			}
			if (user.isAdmin())
				return "ADMIN";
			if (user.isFarmaceut())
				return "FARMACEUT";
			if (user.isVaerkfoerer())
				return "VAERKFOERER";
			//if (user.isOperatoer()) // Skal en operatør kunne logge ind?
			//return "OPERATOER";
			throw new TokenException("Bruger har ingen adgang."); // Hvis brugeren ikke har en gyldig rolle
		} else {
			throw new TokenException("Ugyldig token.");			
		}
	}

	@Override
	public String getUsername(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (th.validateToken(token) != null){
			UserDTO user = dao.getUser(Integer.parseInt(th.getUserID(token)));
			return user.getName();
		} else {
			throw new TokenException("Adgang nægtet");		
		}
	}

	@Override
	public List<UserDTO> getOprList(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		// Tjek om brugeren bag token har adgang til informationen
		if (!getRole(token).equalsIgnoreCase("Admin")) 
			throw new TokenException("Adgang nægtet");

		return dao.getOprList();
	}

	@Override
	public UserDTO updateUser(String token, UserDTO user) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		// Tjek om brugeren bag token har adgang til informationen
		if (!getRole(token).equalsIgnoreCase("Admin")) 
			throw new TokenException("Adgang nægtet");

		if (!FieldVerifier.isValidCpr(user.getCpr()) || !FieldVerifier.isValidInitial(user.getIni()) 
				|| !FieldVerifier.isValidName(user.getName()) 
				|| !FieldVerifier.isValidPassword(user.getPassword()) 
				|| !FieldVerifier.isValidRoles(user))
			throw new DALException("Ugyldigt input");

		return dao.updateUser(user);
	}

	@Override
	public List<RaavareDTO> getRaavareList(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (getRole(token).equalsIgnoreCase("Farmaceut") || getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.getRaavareList();
		else 
			throw new TokenException("Adgang nægtet");		
	}

	@Override
	public void createUser(String token, UserDTO user) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (!FieldVerifier.isValidCpr(user.getCpr()) || !FieldVerifier.isValidInitial(user.getIni()) 
				|| !FieldVerifier.isValidName(user.getName()) 
				|| !FieldVerifier.isValidPassword(user.getPassword()) 
				|| !FieldVerifier.isValidRoles(user))
			throw new DALException("Ugyldigt input");

		if (getRole(token).equalsIgnoreCase("Admin"))
			dao.createUser(user);						
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public List<ProduktBatchDTO> getPBList(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.getProduktBatchList();				
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public List<ProduktBatchKompDTO> getPBKList(String token, int pbID) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (pbID <= 0)
			throw new DALException("Ugyldigt input");

		if (getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.getPBKList(pbID);						
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public void deleteUser(String token, int userId) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (!FieldVerifier.isValidUserId(Integer.toString(userId)))
			throw new DALException("Ugyldigt input");

		if (getRole(token).equalsIgnoreCase("Admin"))
			dao.deleteUser(userId);					
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public void updateRaavare(String token, RaavareDTO raavare) throws TokenException, DALException{
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		
		if (!FieldVerifier.isValidName(raavare.getRaavareNavn()) || !FieldVerifier.isValidRaavareId(Integer.toString(raavare.getRaavareId()))
				|| !FieldVerifier.illigalChars(raavare.getLeverandoer()))
			throw new DALException("Ugyldigt input");

		if (getRole(token).equalsIgnoreCase("Farmaceut"))
			dao.updateRaavare(raavare);
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public List<ReceptDTO> getReceptList(String token) throws TokenException, DALException{
		if(TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Farmaceut") || getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.getReceptList();
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public List<RaavareBatchDTO> getRaavareBatchList(String token) throws TokenException, DALException {
		if(TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.getRaavareBatchList();
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public ProduktBatchDTO createPB(String token, ProduktBatchDTO pb)throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (pb.getStatus() != 0)
			throw new DALException("Ugyldigt input");

		if(getRole(token).equalsIgnoreCase("Vaerkfoerer"))
			return dao.createPB(pb);
		else 
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public List<PbViewDTO> getPbView(String token, int pb_id) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if (pb_id <= 0)
			throw new DALException("Ugyldigt input");

		if(getRole(token).equalsIgnoreCase("Vaerkfoerer")){
			List<PbViewDTO> list = dao.getPbViewList(pb_id);
			return list;
		} else 
			throw new TokenException("Adgang nægtet");
	}

	public List<ReceptKompDTO> getReceptKompListe(String token, int receptid) throws TokenException, DALException{
		if(TEST_DELAY)
			try{
				Thread.sleep(2000);
			}
		catch (InterruptedException e){}

		if (receptid <= 0)
			throw new DALException("Ugyldigt input");

		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			List<ReceptKompDTO> list = dao.getReceptKompListe(receptid);
			return list;
		} else 
			throw new TokenException("Adgang nægtet!");
	}

	@Override
	public void createRecept(String token, ReceptDTO recept) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		if(!FieldVerifier.isValidUserId(Integer.toString(recept.getReceptId())) || !FieldVerifier.isValidReceptName(recept.getReceptNavn())){
			throw new DALException("Ugyldigt input");
		}
		if (getRole(token).equalsIgnoreCase("Farmaceut")){
				dao.createRecept(recept);}
		else {
			throw new TokenException("Adgang nægtet");		
		 }
	}

	@Override
	public void createRaavare(String token, RaavareDTO raavare) throws TokenException, DALException{
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			try{
				dao.createRaavare(raavare);
			}
			catch(DALException e){
				throw new DALException("Råvareid findes allerede!");
			}
		} else
			throw new TokenException("Adgang nægtet");			
	}

	@Override
	public void getRaavareID(String token, int raavareid) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			try{ dao.getRaavareID(raavareid);
			}
			catch(DALException e){
				throw new DALException("Råvareid ukendt!");
			}		
		} else
			throw new TokenException("Adgang nægtet");
	}

	@Override
	public void createReceptKomp(String token, ReceptKompDTO receptkomp) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(!FieldVerifier.isValidUserId(Integer.toString(receptkomp.getReceptId())) 
				|| !FieldVerifier.isValidUserId(Integer.toString(receptkomp.getRaavareId())) 
				|| !FieldVerifier.isValidNetto(Double.toString(receptkomp.getNomNetto())) 
				|| !FieldVerifier.isValidTol(Double.toString(receptkomp.getTolerance()))){
			throw new DALException("Ugyldigt input");
		}
	
		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			dao.createReceptKomp(receptkomp);}
			
		else 
			throw new TokenException("Adgang nægtet");		
	}

	@Override
	public void checkReceptID(String token, int id) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		boolean check = false;
		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			dao.getRecept(id);
			dao.checkReceptID(id);
		} else {
			throw new TokenException("Adgang nægtet");		
		}
	}

	// MÅ IKKE ÆNDRES!!!!! (MVH JON)
	@Override
	public String refreshToken(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		int userId = Integer.parseInt(th.getUserID(token));

		//		UserDTO user = dao.getUser(userId); // Der kastes exception hvis bruger id ikke findes.

		return th.createToken(Integer.toString(userId));
	}

	@Override
	public List<Integer> getUserCount(String token) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Admin"))
			return dao.getUserCount();
		else 
			throw new TokenException("Adgang nægtet");		
	}

	@Override
	public void deleteProduktBatch(String token, int id) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		boolean check = false;
		if(getRole(token).equalsIgnoreCase("Vaerkfoerer")){
			dao.getProduktBatch(id);
			try{
				dao.deleteProduktBatch(id);
			} catch (Exception DALException){
				check = true;
			}
		}
		if (check)
			throw new DALException("Produktbatchen er påbegyndt og kan ikke slettes.");
	}

	@Override
	public void createRaavareBatch(String token, RaavareBatchDTO raavareBatch) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

		if(getRole(token).equalsIgnoreCase("Vaerkfoerer")){
			try{
				dao.createRaavareBatch(raavareBatch);
			}
			catch(DALException e){
				throw new DALException("Råvare Batch findes allerede!");
			}
		}

	}

	@Override
	public Integer getUserId(String token) throws TokenException, DALException {
		return Integer.parseInt(th.getUserID(token));
	}

	@Override
	public void deleteRaavare(String token, int raavare_id) throws TokenException, DALException {
		if (TEST_DELAY)
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}
		if(getRole(token).equalsIgnoreCase("Farmaceut")){
			dao.getRaavareID(raavare_id);
			dao.deleteRaavare(raavare_id);
		}
	}
}
