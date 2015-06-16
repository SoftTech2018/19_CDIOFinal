package cdio.shared;

public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		if (!illigalChars(name))
			return false;

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

	/**
	 * Tjek om bruger id er valid
	 * @param id
	 * @return
	 */
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

	/**
	 * Tjek om password er valid
	 * @param pass
	 * @return
	 */
	public static boolean isValidPassword(String pass){
		if (pass == null)
			return false;
		if (pass.length()<4)
			return false;

		if (!illigalChars(pass))
			return false;

		return true;
	}

	/**
	 * Tjek om initialer er valid
	 * @param ini
	 * @return True hvis initial er valid
	 */
	public static boolean isValidInitial(String ini){
		if (ini == null)
			return false;
		if (ini.length()>3)
			return false;
		if (!illigalChars(ini))
			return false;
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
		String ny = netto;
		for(int i=0; i<netto.length(); i++){
			if(netto.charAt(i)==','){
				ny = netto.replace(",", ".");
			}
		}			
		for(int i = 0; i>ny.length(); i++){
			if(ny.charAt(i)=='.'){
				String[] sString = ny.split("\\.");
				if(sString[1].length()>4){
					return false;				
				}
		try{
			Double input =	Double.parseDouble(ny); 

			if(input < 0.05){
				return false;
			}
			if(input >= 20.1 ){
				return false;
			}		
		} catch (NumberFormatException e){
			return false;
		}
		
		
		
		}
		}
		return true;		
	}

	public static boolean isValidTol(String tol){
		String ny = tol;
		for(int i=0; i<tol.length(); i++){
			if(tol.charAt(i)==','){
				ny = tol.replace(",", ".");
			}
		}		
		try{		
			Double input =	Double.parseDouble(ny); 

			if(input < 0.1){
				return false;
			}
			if(input >= 10.0 ){
				return false;
			}		
		} catch (NumberFormatException e){
			return false;
		}
		String[] sString = ny.split("\\.");
		if(sString[1].length()>2){
			return false;
		}
		return true;		
	}

	/**
	 * Tjek om et receptnavn er valid
	 * @param name navnet der skal tjekkes
	 * @return True hvis navn er valid
	 */
	public static boolean isValidReceptName(String name) {
		for (int i=0; i < name.length(); i++){
			String sString = name.substring(i, i+1);
			if (sString.matches("[0-9]")){
				return false;
			}
			if (!illigalChars(name))
				return false;
			if (name.length() <= 1) {
				return false;			
			}

		}
		// max 22 karakterer
		return name.length() <= 22;
	}

	/**
	 * Tjek om et råvare id er valid
	 * @param id
	 * @return
	 */
	public static boolean isValidRaavareId(String id){
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

	/**
	 * Tjek om et råvare batch id er valid
	 * @param id
	 * @return
	 */
	public static boolean isValidRaavareBatchId(String id){
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

	/**
	 * Tjek om mængde er valid
	 * @param id
	 * @return
	 */
	public static boolean isValidMaengde(String id){
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

	public static boolean isValidRaavareName(String name) {
		if (name.length() <= 1) {
			return false;			
		}
		if (name.length() > 22){
			return false;
		}
		if (!illigalChars(name))
			return false;
		for (int i=0; i < name.length(); i++){
			String sString = name.substring(i, i+1);
			if (sString.matches("[0-9]"))
				return false;
		}		
		return true;
	}

	public static boolean isValidLeverandorName(String name) {
		for (int i=0; i < name.length(); i++){
			String sString = name.substring(i, i+1);
			if (sString.matches("[0-9]")){
				return false;
			}
			if (!illigalChars(name))
				return false;
			if (name.length() <= 1) {
				return false;			
			}

		}
		// max 22 karakterer
		return name.length() <= 22;
	}

	/**
	 * Tjek om en brugers rolle(r) er valid. 
	 * @param user Brugeren der skal tjekkes
	 * @return True hvis brugeren har netop én rolle
	 */
	public static boolean isValidRoles(UserDTO user) {
		int count = 0;
		if(user.isAdmin())
			count++;
		if(user.isFarmaceut())
			count++;
		if(user.isVaerkfoerer())
			count++;
		if(user.isOperatoer())
			count++;

		if(count != 1)
			return false;
		else 
			return true;
	}

	/**
	 * Tjek om en string indeholder et eller flere ulovlige tegn
	 * @param string
	 * @return True hvis stringen IKKE indeholder ulovlige tegn
	 */
	public static boolean illigalChars(String string){
		for (int i=0; i<string.length(); i++){
			char p = string.charAt(i);
			if (p=='#' || p=='<' || p=='>' || p=='"' || p=='&')
				return false;
		}
		return true;
	}
}
