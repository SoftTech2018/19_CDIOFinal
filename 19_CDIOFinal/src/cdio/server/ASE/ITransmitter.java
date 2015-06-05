package cdio.server.ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface ITransmitter {

	/**
	 * Viser op til 3 RM20 tekster på vægten og afventer et svar fra operatøren.
	 * @param txt1
	 * @param txt2
	 * @param txt3
	 * @return Svaret fra operatøren
	 * @throws IOException 
	 */
	public abstract String RM20(String txt1, String txt2, String txt3) throws IOException;

	/**
	 * Viser en tekst på vægten
	 * @param txt
	 * @return Returbesked fra vægt (ES = fejl)
	 * @throws IOException 
	 */
	public abstract boolean P111(String txt) throws IOException;

	/**
	 * Beder om aflæsning af vægten
	 * @return Den aflæste vægt
	 * @throws IOException 
	 */
	public abstract String S() throws IOException;

	/**
	 * Tarer vægten
	 * @return
	 * @throws IOException 
	 */
	public abstract String T() throws IOException;

	public abstract boolean D(String txt) throws IOException;

	public abstract boolean DW() throws IOException;

	void connected(BufferedReader in, PrintWriter out);

	boolean startST(boolean status) throws IOException;

	String listenST() throws IOException;

}