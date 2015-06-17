package cdio;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cdio.server.DAL.Connector;
import cdio.server.DAL.TextReader;
import cdio.server.DAL.dao.ProduktBatchKompDAO;
import cdio.server.DAL.idao.IProduktBatchKompDAO;
import cdio.shared.DALException;
import cdio.shared.ProduktBatchKompDTO;

public class ProduktBatchKompDAOTest {
	
	IProduktBatchKompDAO pbkDAO;

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
		pbkDAO = new ProduktBatchKompDAO(txt);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProduktBatchKomp() {
		try {
			int pbID = pbkDAO.getProduktBatchKomp(1,1).getPbId();
			int rbID = pbkDAO.getProduktBatchKomp(1,1).getRbId();
			assertEquals(1,pbID);
			assertEquals(1,rbID);
		} catch (DALException e) {e.printStackTrace();}	
	}

	@Test
	public void testGetProduktBatchKompList() {
		
	}

	@Test
	public void testCreateProduktBatchKomp() {
		int pbID = 0, rbID = 0, tempPB = 0, tempRB = 0;
		try {
			for(ProduktBatchKompDTO pbkDto : pbkDAO.getProduktBatchKompList()){
				tempPB = pbkDto.getPbId();
				tempRB = pbkDto.getRbId();
				if (tempPB>pbID){
					pbID = tempPB;
				}
				if (tempRB>pbID){
					rbID = tempRB;
				}
			}
			pbID++;
			double tara = pbID+100.99;
			double netto = rbID+100.99;
			pbkDAO.createProduktBatchKomp(new ProduktBatchKompDTO(pbID, rbID, tara, netto, 1, "123.123.123.123"));
			assertEquals(tara, pbkDAO.getProduktBatchKomp(pbID, rbID).getTara(), 0.01);
			assertEquals(netto, pbkDAO.getProduktBatchKomp(pbID, rbID).getNetto(), 0.01);
		} catch (DALException e) {e.printStackTrace();}
	}

	@Test
	public void testUpdateProduktBatchKomp() {
		int pbID = 0, rbID = 0;
		try {
			for(ProduktBatchKompDTO pbkDto : pbkDAO.getProduktBatchKompList()){
				if (String.valueOf(pbkDto.getTara()).startsWith("10")){
					pbID = pbkDto.getPbId();
					rbID = pbkDto.getRbId();
					break;
				}				
			}
			double tara = pbkDAO.getProduktBatchKomp(pbID, rbID).getTara();
			pbkDAO.updateProduktBatchKomp(new ProduktBatchKompDTO(pbID, rbID, tara+400,pbkDAO.getProduktBatchKomp(pbID, rbID).getNetto(), pbkDAO.getProduktBatchKomp(pbID, rbID).getOprId(), "123.123.123.123"));
			assertEquals(400+tara, pbkDAO.getProduktBatchKomp(pbID, rbID).getTara(), 0.01);
		} catch (DALException e) {e.printStackTrace();}	
	}

}
