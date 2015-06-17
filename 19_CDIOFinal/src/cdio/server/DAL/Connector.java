package cdio.server.DAL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cdio.shared.DALException;

public class Connector{

	private final String
	/*
	 * Dette er den centrale Database stillet til rådighed af Stig/Ronnie
	 */
	//	server					= "62.79.16.16",  // database-serveren
	//	database				= "grp19",  //"jdbcdatabase", // navnet paa din database = dit studienummer
	//	username				= "grp19", // dit brugernavn = dit studienummer 
	//	password				= "WxqW2GBF"; // dit password som du har valgt til din database
	
	/*
	 * Dette er den lokale Database på din egen computer
	 */
	server					= "localhost",  // database-serveren
	database				= "cdio_db",  //"jdbcdatabase", // navnet paa din database = dit studienummer
	username				= "root", // dit brugernavn = dit studienummer 
	password				= ""; // dit password som du har valgt til din database

	private final int port = 3306;

	private Connection conn;
	private static Statement stm;
	private static ScriptRunner runner;

	public Connector() throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, SQLException {
		conn	= connectToDatabase("jdbc:mysql://"+server+":"+port+"/"+database, username, password);
		stm		= conn.createStatement();
		runner = new ScriptRunner(conn,false,true);
	}

	/**
	 * To connect to a MySQL-server
	 * 
	 * @param url must have the form
	 * "jdbc:mysql://<server>/<database>" for default port (3306)
	 * OR
	 * "jdbc:mysql://<server>:<port>/<database>" for specific port
	 * more formally "jdbc:subprotocol:subname"
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	private Connection connectToDatabase(String url, String username, String password)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException{
		// call the driver class' no argument constructor
		Class.forName("com.mysql.jdbc.Driver").newInstance();

		// get Connection-object via DriverManager
		return (Connection) DriverManager.getConnection(url, username, password);
	}

	public static ResultSet doQuery(String cmd) throws DALException	{
		try { 
			return stm.executeQuery(cmd); 
		} catch (SQLException e) { 
			throw new DALException(e); 
		}
	}

	public static int doUpdate(String cmd) throws DALException {
		try { 
			return stm.executeUpdate(cmd); 
		}
		catch (SQLException e) { 
			throw new DALException(e); 
		}
	}
	
	public static void runScript() throws DALException {
		try {
			runner.runScript(new BufferedReader(new FileReader("/Users/JacobWorckJepsen/Dropbox/CDIO/2. Semester/Videregående Programmering/FInal/Database/Script/cdio_db5.sql")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}