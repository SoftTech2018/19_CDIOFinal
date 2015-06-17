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
import cdio.server.DAL.dao.ReceptDAO;
import cdio.server.DAL.idao.IReceptDAO;
import cdio.shared.DALException;
import cdio.shared.ReceptDTO;

public class ReceptDAOTest {
	
	IReceptDAO rDAO;

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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetRecept() {
		try {
			int rcpID = rDAO.getRecept(1).getReceptId();
			assertEquals(1,rcpID);
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testGetReceptList() {
		try {
			boolean liste;
			liste = rDAO.getReceptList().isEmpty();
			assertEquals(false, liste);
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateRecept() {
		int rcpID = 0, temp = 0;
		try {
			for(ReceptDTO rcpDto : rDAO.getReceptList()){
				temp = rcpDto.getReceptId();
				if (temp>rcpID){
					rcpID = temp;	
				}				
			}
			rcpID++;
			String test = "test"+rcpID;
			rDAO.createRecept(new ReceptDTO(rcpID, test));
			assertEquals(test, rDAO.getRecept(rcpID).getReceptNavn());
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testUpdateRecept() {
		int rcpID = 0;
		try {
			for(ReceptDTO rcpDto : rDAO.getReceptList()){
				if (rcpDto.getReceptNavn().startsWith("test")){
					rcpID = rcpDto.getReceptId();
					break;
				}				
			}
			rDAO.updateRecept(new ReceptDTO(rcpID, "update "+rDAO.getRecept(rcpID).getReceptNavn()));
			assertEquals("update test"+rcpID, rDAO.getRecept(rcpID).getReceptNavn());
		} catch (DALException e) {e.printStackTrace();}	
	}

}
