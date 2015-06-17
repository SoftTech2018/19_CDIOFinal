package cdio;

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
import cdio.server.DAL.dao.ReceptDAO;
import cdio.server.DAL.dao.ReceptKompDAO;
import cdio.server.DAL.idao.IReceptDAO;
import cdio.server.DAL.idao.IReceptKompDAO;
import cdio.shared.DALException;
import cdio.shared.ReceptDTO;
import cdio.shared.ReceptKompDTO;

public class ReceptKompDAOTest {
	
	IReceptDAO rDAO;
	IReceptKompDAO rkDAO;

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
		rDAO = new ReceptDAO(txt);
		rkDAO = new ReceptKompDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetReceptKomp() {
		try {
			int id = 0;
			for (ReceptKompDTO rDto : rkDAO.getReceptKompList()){
				id = rDto.getRaavareId();
			}
			assertEquals(id, rkDAO.getReceptKomp(3, 7).getRaavareId());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetReceptKompList() {
		try {
			boolean liste;
			liste = rkDAO.getReceptKompList().isEmpty();
			assertEquals(false, liste);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateReceptKomp() {
		try {
			rkDAO.createReceptKomp(new ReceptKompDTO(3, 2, 0.1, 0.1));
			int raavareid = 0, receptid = 0;
			for (ReceptKompDTO rDto : rkDAO.getReceptKompList()){
				raavareid = rDto.getRaavareId();
				receptid = rDto.getReceptId();
			}
			assertEquals(receptid, rkDAO.getReceptKomp(receptid, raavareid).getReceptId());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
