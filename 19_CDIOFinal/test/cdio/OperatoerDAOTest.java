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
import cdio.shared.UserDTO;

public class OperatoerDAOTest {

	IOperatoerDAO oprDAO;
	TextReader txt;
	Connector conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Connector.runScript();
	}

	@Before
	public void setUp() throws Exception {
		try {new Connector();} 
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
		}
	}

	@Test
	public void testCreateOperatoer() {
		int oprID = 1, temp = 0;
		try {
			for(UserDTO oprDto : oprDAO.getOperatoerList()){
				temp = oprDto.getUserId();
				if (temp>oprID){
					oprID = temp;	
				}				
			}
			oprID++;
			oprDAO.createOperatoer(new UserDTO(Integer.toString(oprID), "test"+Integer.toString(oprID), "test"+Integer.toString(oprID), "test"+Integer.toString(oprID), "test"+Integer.toString(oprID), false, true, false, false));
			assertEquals("test"+oprID, oprDAO.getOperatoer(oprID).getName());
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testUpdateOperatoer() {
		int oprID = 0;
		try {
			for(UserDTO oprDto : oprDAO.getOperatoerList()){
				if (oprDto.getName().startsWith("test")){
					oprID = oprDto.getUserId();
					break;
				}				
			}
			System.out.println(oprID);
			oprDAO.updateOperatoer(new UserDTO(Integer.toString(oprID), "update test"+Integer.toString(oprID), "update test"+Integer.toString(oprID), "update test"+Integer.toString(oprID), "update test"+Integer.toString(oprID), false, true, false, false));
			assertEquals("update test"+oprID, oprDAO.getOperatoer(oprID).getName());
		} catch (DALException e) {e.printStackTrace();}		
	}

	@Test
	public void testGetOperatoerList() {
		try {
			boolean oprList = oprDAO.getOperatoerList().isEmpty();
			assertEquals(false, oprList);
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAdminCount() {
		try {
			int oprID = 0, temp = 0;
				for(UserDTO oprDto : oprDAO.getOperatoerList()){
					temp = oprDto.getUserId();
					if (temp>oprID){
						oprID = temp;	
					}				
				}
				oprID++;
				int admCount = oprDAO.getAdminCount();
				oprDAO.createOperatoer(new UserDTO(Integer.toString(oprID), "admin"+Integer.toString(oprID), "admin"+Integer.toString(oprID), "admin"+Integer.toString(oprID), "admin"+Integer.toString(oprID), true, false, false, false));
				assertEquals(admCount+1, oprDAO.getAdminCount());
			} catch (DALException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Test
		public void testGetFarmaceutCount() {
			try {
				int oprID = 0, temp = 0;
					for(UserDTO oprDto : oprDAO.getOperatoerList()){
						temp = oprDto.getUserId();
						if (temp>oprID){
							oprID = temp;	
						}				
					}
					oprID++;
					int farmCount = oprDAO.getFarmaceutCount();
					oprDAO.createOperatoer(new UserDTO(Integer.toString(oprID), "farm"+Integer.toString(oprID), "farm"+Integer.toString(oprID), "farm"+Integer.toString(oprID), "farm"+Integer.toString(oprID), false, true, false, false));
					assertEquals(farmCount+1, oprDAO.getFarmaceutCount());
				} catch (DALException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		@Test
		public void testGetVaerkfoererCount() {
			try {
				int oprID = 0, temp = 0;
					for(UserDTO oprDto : oprDAO.getOperatoerList()){
						temp = oprDto.getUserId();
						if (temp>oprID){
							oprID = temp;	
						}				
					}
					oprID++;
					int varkCount = oprDAO.getVaerkfoererCount();
					oprDAO.createOperatoer(new UserDTO(Integer.toString(oprID), "vark"+Integer.toString(oprID), "vark"+Integer.toString(oprID), "vark"+Integer.toString(oprID), "vark"+Integer.toString(oprID), false, false, true, false));
					assertEquals(varkCount+1, oprDAO.getVaerkfoererCount());
				} catch (DALException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		@Test
		public void testGetOperatoerCount() {
			try {
				int oprID = 0, temp = 0;
					for(UserDTO oprDto : oprDAO.getOperatoerList()){
						temp = oprDto.getUserId();
						if (temp>oprID){
							oprID = temp;	
						}				
					}
					oprID++;
					int oprCount = oprDAO.getOperatoerCount();
					oprDAO.createOperatoer(new UserDTO(Integer.toString(oprID), "opr"+Integer.toString(oprID), "opr"+Integer.toString(oprID), "opr"+Integer.toString(oprID), "opr"+Integer.toString(oprID), false, false, false, true));
					assertEquals(oprCount+1, oprDAO.getOperatoerCount());
				} catch (DALException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
