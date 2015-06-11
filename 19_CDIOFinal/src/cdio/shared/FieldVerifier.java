package cdio.shared;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client-side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		for (int i=0; i < name.length(); i++){
			String sString = name.substring(i, i+1);
			if (sString.matches("[0-9]")){
				return false;
			}
		}
		if (name.length() == 0) {
			return false;			
		}
		// max 30 karakterer
		return name.length() <= 30;
	}

	public static boolean isValidUserId(String id){
		try {
			int i = Integer.parseInt(id);
			if(i<=0 || i>99999999){
				throw new NumberFormatException();
			}
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}

	public static boolean isValidPassword(String pass){
		if (pass == null)
			return false;
		if (pass.length()<4)
			return false;
		for (int i=0; i<pass.length(); i++){
			char x = pass.charAt(i);
			if (x == '&' || x == '<' || x == '>'){
				return false;
			}
		}

		return true;
	}

	public static boolean isValidInitial(String ini){
		return true;
	}

	public static boolean isValidCpr(String cpr){
		for (int i=0; i < cpr.length(); i++){
			String sString = cpr.substring(i, i+1);
			if (sString.matches("[a-z]")){
				return false;
			} else if (sString.matches("[!]")){

				return false;
			}
		}
		if (cpr.length() < 11 || cpr.length() > 11){
			return false;
		}
		if (!(cpr.charAt(6)=='-')){
			return false;
		}
		return true;
	}

	public static boolean isValidNetto(String netto){
		String ny = netto.replaceAll(",",".");
		String[] sString = ny.split("\\.");
		if(sString[1].length()>4){
			return false;
		}
			
		try{
			
			Double input =	Double.parseDouble(ny); 
			if(input < 0.5){
				return false;
			}
			if(input >= 21 ){
				return false;
			}		
		} catch (NumberFormatException e){
			return false;
		}
		
		return true;		
	}


	
	
	

	public static boolean isValidReceptName(String name) {
		for (int i=0; i < name.length(); i++){
			String sString = name.substring(i, i+1);
			if (sString.matches("[0-9]")){
				return false;
			}
			for (int j=0; i<name.length(); i++){
				char x = name.charAt(i);
				if (x == '&' || x == '<' || x == '>'){
					return false;
				}	

			}
			if (name.length() <= 1) {
				return false;			
			}

		}
		// max 22 karakterer
		return name.length() <= 22;
	}
}
