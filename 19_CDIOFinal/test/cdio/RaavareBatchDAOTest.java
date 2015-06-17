package cdio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.dao.RaavareBatchDAO;
import cdio.server.DAL.idao.IRaavareBatchDAO;
import cdio.shared.DALException;
import cdio.shared.RaavareBatchDTO;

public class RaavareBatchDAOTest {
	
	IRaavareBatchDAO rbDAO;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Connector.runScript();
	}

	@Before
	public void setUp() throws Exception {
		try { new Connector(); } 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (SQLException e) { e.printStackTrace(); }
		TextReader txt = new TextReader("war");
		rbDAO = new RaavareBatchDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRaavareBatch() {
		try {
			int rbID = rbDAO.getRaavareBatch(1).getRbId();
			assertEquals(1,rbID);
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testGetRaavareBatchList() {
		boolean liste;
		try {
			liste = rbDAO.getRaavareBatchList().isEmpty();
			assertEquals(false, liste);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateRaavareBatch() {
		int rbID = 0, temp = 0;
		try {
			for(RaavareBatchDTO rbDto : rbDAO.getRaavareBatchList()){
				temp = rbDto.getRbId();
				if (temp>rbID){
					rbID = temp;
				}				
			}
			rbID++;
			double m = 10000+rbID;
			rbDAO.createRaavareBatch(new RaavareBatchDTO(rbID, rbDAO.getRaavareBatch(temp).getRaavareId(), m));
			assertEquals(m, rbDAO.getRaavareBatch(rbID).getMaengde(), 0.01);
		} catch (DALException e) {e.printStackTrace();}
	}

	@Test
	public void testUpdateRaavareBatch() {
		try {
			int rbID = 0;
			for(RaavareBatchDTO rbDto : rbDAO.getRaavareBatchList()){
					rbID = rbDto.getRbId();			
			}
			double m = 10000+rbDAO.getRaavareBatch(rbID).getMaengde();
			rbDAO.updateRaavareBatch(new RaavareBatchDTO(rbID, rbDAO.getRaavareBatch(rbID).getRaavareId(), m));
			assertEquals(m, rbDAO.getRaavareBatch(rbID).getMaengde(), 0.01);
		} catch (DALException e) {e.printStackTrace();}	
	}

}
