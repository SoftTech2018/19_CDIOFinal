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
import cdio.server.DAL.dao.OperatoerDAO;
import cdio.server.DAL.dao.ReceptKompDAO;
import cdio.server.DAL.idao.IOperatoerDAO;
import cdio.shared.DALException;

public class OperatoerDAOTest {
	
	IOperatoerDAO oprDAO;
	TextReader txt;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		try { new Connector(); } 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (SQLException e) { e.printStackTrace(); }
		txt = new TextReader("war");
		oprDAO = new OperatoerDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOperatoer() {
		try {
			int oprID = oprDAO.getOperatoer(1).getUserId();
			assertEquals(1,oprID);
		} catch (DALException e) {
			e.printStackTrace();
			//hej
		}
	}

	@Test
	public void testCreateOperatoer() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateOperatoer() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOperatoerList() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetListViewOpr() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetView() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetProcedure() {
		fail("Not yet implemented");
	}

	@Test
	public void testCallProcedure() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetFunction() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFunction() {
		fail("Not yet implemented");
	}

	@Test
	public void testDropAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAdminCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFarmaceutCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVaerkfoererCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOperatoerCount() {
		fail("Not yet implemented");
	}

}
