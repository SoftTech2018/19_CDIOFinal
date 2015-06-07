package cdio.server.DAL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextReader {
	
	private File sqlFileCommands;
	private String[] sqlCommands;
	private String illegalString;
	
	public TextReader() throws FileNotFoundException{
		sqlFileCommands = new File("WEB-INF/sqlCommands.txt");
		sqlCommands = readFile(sqlFileCommands);
		illegalString = "#";
	}
	
	private String[] readFile(File fil) throws FileNotFoundException{
		List<String> data = new ArrayList<String>();
		String linje = null;
		try (BufferedReader br = new BufferedReader(new FileReader(fil));){
			while ((linje = br.readLine()) != null){
				data.add(linje);
			}
		} catch (IOException e) {
			throw new FileNotFoundException();
		}
		return data.toArray(new String[data.size()]);
	}
	
	/**
	 * Returnerer den kommando der svarer til teksten på linje-nummeret i filen "files/sqlCommands.txt". 
	 * @param cmd Linjenummeret der skal returneres
	 * @return Teksten på linjenummeret
	 */
	public String getCommand(int cmd){
		return sqlCommands[cmd-1];
	}
	
	public String getOperatoer(int oprID){
		String output = sqlCommands[0];
		output = output.replaceFirst(illegalString + "1", Integer.toString(oprID));
		return output;
	}
	
	public String createOperatoer(DTO opr){
		String output = sqlCommands[1];
		output = output.replaceFirst(illegalString + "1", Integer.toString(opr.getOprId())); // OprID
		output = output.replaceFirst(illegalString + "2", opr.getOprNavn());
		output = output.replaceFirst(illegalString + "3", opr.getIni());
		output = output.replaceFirst(illegalString + "4", opr.getCpr());
		output = output.replaceFirst(illegalString + "5", opr.getPassword());
		return output;
	}
	
	public String updateOperatoer(DTO opr){
		String output = sqlCommands[2];
		output = output.replaceFirst(illegalString + "1", opr.getOprNavn());
		output = output.replaceFirst(illegalString + "2", opr.getIni());
		output = output.replaceFirst(illegalString + "3", opr.getCpr());
		output = output.replaceFirst(illegalString + "4", opr.getPassword());
		output = output.replaceFirst(illegalString + "5", Integer.toString(opr.getOprId())); // OprID
		return output;
	}

}
