package cdio;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.dao.RaavareDAO;
import cdio.server.DAL.idao.IRaavareDAO;
import cdio.shared.DALException;
import cdio.shared.RaavareDTO;

public class RaavareDAOTest {
	
	IRaavareDAO rDAO;

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
		rDAO = new RaavareDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRaavare() {
		try {
			assertEquals("dej", rDAO.getRaavare(1).getRaavareNavn());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetRaavareList() {
		try {
			boolean liste = rDAO.getRaavareList().isEmpty();
			assertEquals(false, liste);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateRaavare() {
		try {
			int i = 0;
			for (RaavareDTO rDto : rDAO.getRaavareList()){
				i++;
			}
			rDAO.createRaavare(new RaavareDTO(i+1, "pubad", "Leverandoer"));
			assertEquals("pubad", rDAO.getRaavare(i+1).getRaavareNavn());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateRaavare() {
		try {
			int rID = 0;
			for(RaavareDTO rDto : rDAO.getRaavareList()){
					rID = rDto.getRaavareId();	
			}
			String update = "update "+rDAO.getRaavare(rID).getRaavareNavn();
			rDAO.updateRaavare(new RaavareDTO(rID, update, update));
			assertEquals(update, rDAO.getRaavare(rID).getRaavareNavn());
		} catch (DALException e) {e.printStackTrace();}
	}

}
