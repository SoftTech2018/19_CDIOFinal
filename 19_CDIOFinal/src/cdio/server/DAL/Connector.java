package cdio.server.DAL;

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

	public Connector() throws InstantiationException, IllegalAccessException,
	ClassNotFoundException, SQLException {
		conn	= connectToDatabase("jdbc:mysql://"+server+":"+port+"/"+database, username, password);
		stm		= conn.createStatement();
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
}