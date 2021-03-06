package ASE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Transmitter implements ITransmitter {
	
	private BufferedReader in;
	private PrintWriter out;
	
	@Override
	public void connected(BufferedReader in, PrintWriter out){
		this.in = in;
		this.out = out;
//		try {
//			System.out.println("pre rd.ln");
//			System.out.println(in.readLine());
//			System.out.println("post rd.ln");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#RM20(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String RM20(String txt1, String txt2, String txt3) throws IOException{
		out.println("RM20 8" + " \"" + txt1 + "\" \"" + txt2 + "\" \"" + txt3 + "\"" );
		String reply = in.readLine();
		System.out.println("RM20 svar: "+reply);
		String error = "ES";
		String cancel = "q";			
		
		if (reply.equalsIgnoreCase("RM20 B")){
			String input = in.readLine();
			if (input.equalsIgnoreCase("RM20 C")){
				return cancel;
			}
			if (input.substring(0, 6).equalsIgnoreCase("RM20 A")){
				return input.substring(8,(input.length()-1)); // Skal muligvis være 6				
			}
			return error;
		} else if (reply.equalsIgnoreCase("RM20 L")){
			return "L";
		} else {
			return error;
		}
	}
	
	@Override
	public String RM20int(String txt1, String txt2, String txt3) throws IOException{
		out.println("RM20 3" + " \"" + txt1 + "\" \"" + txt2 + "\" \"" + txt3 + "\"" );
		String reply = in.readLine();
		System.out.println("RM20 svar: "+reply);
		String error = "ES";
		String cancel = "q";			
		
		if (reply.equalsIgnoreCase("RM20 B")){
			String input = in.readLine();
			if (input.equalsIgnoreCase("RM20 C")){
				return cancel;
			}
			if (input.substring(0, 6).equalsIgnoreCase("RM20 A")){
				return input.substring(8,(input.length()-1)); // Skal muligvis være 6				
			}
			return error;
		} else if (reply.equalsIgnoreCase("RM20 L")){
			return "L";
		} else {
			return error;
		}
	}
	
	public void RM20cancel() throws IOException{
		out.println("RM20 0");
		String reply = in.readLine();
		System.out.println("RM20 cancel: "+reply);
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#P111(java.lang.String)
	 */
	@Override
	public boolean P111(String txt) throws IOException{
		out.println("P111 \"" + txt + "\"");
		if (in.readLine().equalsIgnoreCase("P111 A")){
			return true;
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#S()
	 */
	@Override
	public String S() throws IOException{
		out.println("S");
		String reply = in.readLine();
		return reply.substring(9,(reply.length()-3));
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#T()
	 */
	@Override
	public String T() throws IOException{
		out.println("T");
		String reply = in.readLine();
		return reply.substring(9,(reply.length()-3));
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#D()
	 */
	@Override
	public boolean D(String txt) throws IOException{
		boolean output = false; 
		if (txt.length() < 8 ){
			out.println("D \"" + txt + "\"");
			if (in.readLine().equalsIgnoreCase("D A")){
				output = true;
			}
		} else{
			output = false;
		}
		return output;
	}
	
	/* (non-Javadoc)
	 * @see functionality.ITransmitter#DW()
	 */
	@Override
	public boolean DW() throws IOException{
		out.println("DW");
		if (in.readLine().equalsIgnoreCase("DW A")){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean startST(boolean status) throws IOException{
		if (status)
			out.println("ST 1");
		else
			out.println("ST 0");
		if (in.readLine().equalsIgnoreCase("ST A"))
			return true;
		else
			return false;
		}
	
	@Override
	public String listenST() throws IOException{
		out.println("ST");
		in.readLine();
		String reply = in.readLine();
		System.out.println(reply);
		System.out.println(reply.charAt(2));
		if(reply.charAt(2)=='-'){
			throw new IOException();
		} else if(reply.charAt(8)=='-'){
			throw new IOException();
		} else {			
			return reply.substring(9,(reply.length()-3));
		}
	}
	
}
