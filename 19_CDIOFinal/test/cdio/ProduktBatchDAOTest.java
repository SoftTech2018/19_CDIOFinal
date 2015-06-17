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
import cdio.server.DAL.dao.ProduktBatchDAO;
import cdio.server.DAL.idao.IProduktBatchDAO;
import cdio.shared.DALException;
import cdio.shared.ProduktBatchDTO;

public class ProduktBatchDAOTest {
	
	IProduktBatchDAO pbDAO;

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
		TextReader txt = new TextReader("war");
		pbDAO = new ProduktBatchDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProduktBatch() {
		try {
			int pbID = pbDAO.getProduktBatch(1).getPbId();
			assertEquals(1,pbID);
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testGetProduktBatchList() {
		int pbID = 0, temp = 0;
		try {
			for(ProduktBatchDTO pbDto : pbDAO.getProduktBatchList()){
				temp = pbDto.getPbId();
				if (temp>pbID){
					pbID = temp;
				}				
			}
			assertEquals(pbID, pbDAO.getProduktBatchList().size());
		} catch (DALException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateProduktBatch() {
		int pbID = 0, temp = 0;
		try {
			for(cdio.shared.ProduktBatchDTO pbDto : pbDAO.getProduktBatchList()){
				temp = pbDto.getPbId();
				if (temp>pbID){
					pbID = temp;
				}				
			}
			pbID++;
			pbDAO.createProduktBatch(new ProduktBatchDTO(pbID, 100+pbID, pbDAO.getProduktBatch(temp).getReceptId(), "16-06-2015", "16-06-2015", "16-06-2015"));
			assertEquals(100+pbID, pbDAO.getProduktBatch(pbID).getStatus());
		} catch (DALException e) {e.printStackTrace();}
	}

	@Test
	public void testUpdateProduktBatch() {
		int pbID = 1;
		try {
			for(ProduktBatchDTO pbDto : pbDAO.getProduktBatchList()){
				if (String.valueOf(pbDto.getStatus()).startsWith("10")){
					pbID = pbDto.getPbId();
					break;
				}				
			}
			pbDAO.updateProduktBatch(new ProduktBatchDTO(pbID, 500+pbID, pbDAO.getProduktBatch(pbID).getReceptId(), "16-06-2015", "16-06-2015", "16-06-2015"));
			assertEquals(500+pbID, pbDAO.getProduktBatch(pbID).getStatus());
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testUpdateStatus() {
		try {
			pbDAO.createProduktBatch(new ProduktBatchDTO(25, 0, 3, "16-06-2015", "16-06-2015", "16-06-2015"));
			pbDAO.updateStatus(pbDAO.getLatestPbId(), 2);
			assertEquals(2, pbDAO.getProduktBatch(pbDAO.getLatestPbId()).getStatus());
		} catch (DALException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetLatestPbId() {
		int pbID = 0, temp = 0;
		try {
			for(ProduktBatchDTO pbDto : pbDAO.getProduktBatchList()){
				temp = pbDto.getPbId();
				if (temp>pbID){
					pbID = temp;
				}				
			}
			pbID++;
			pbDAO.createProduktBatch(new ProduktBatchDTO(pbID, 0, 3, "16-06-2015", "16-06-2015", "16-06-2015"));
			assertEquals(pbID, pbDAO.getLatestPbId());
			
		} catch (DALException e) {e.printStackTrace();}
	}

}
