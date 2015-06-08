package cdio.server.DAL.dto;

import java.io.Serializable;

public class OperatoerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Operatoer-identifikationsnummer (opr_id) i omraadet 1-99999999. Vaelges af brugerne */
	private int oprId;                     
	/** Operatoernavn (opr_navn) min. 2 max. 20 karakterer */
	private String navn;   
	/** Operatoer-initialer min. 2 max. 3 karakterer */
	private String ini;                 
	/** Operatoer cpr-nr 10 karakterer */
	private String cpr;                 
	/** Operatoer password min. 7 max. 8 karakterer */
	private String password;

	private boolean admin, operatoer, farmaceut, varkforer;
	
	public OperatoerDTO(){
		
	}

	public OperatoerDTO(int oprId, String oprNavn, String ini, String cpr, String password, boolean admin, boolean operatoer, boolean farmaceut, boolean varkforer)
	{
		this.oprId = oprId;
		this.navn = oprNavn;
		this.ini = ini;
		this.cpr = cpr;
		this.password = password;
		this.admin = admin;
		this.operatoer = operatoer;
		this.farmaceut = farmaceut;
		this.varkforer = varkforer;
	}

	public OperatoerDTO(OperatoerDTO opr)
	{
		this.oprId = opr.getOprId();
		this.navn = opr.getNavn();
		this.ini = opr.getIni();
		this.cpr = opr.getCpr();
		this.password = opr.getPassword();
		this.admin = opr.isAdmin();
		this.operatoer = opr.isOperatoer();
		this.farmaceut = opr.isFarmaceut();
		this.varkforer = opr.isVarkforer();
	}

	public String getNavn() {
		return navn;
	}

	public void setNavn(String navn) {
		this.navn = navn;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isOperatoer() {
		return operatoer;
	}

	public void setOperatoer(boolean operatoer) {
		this.operatoer = operatoer;
	}

	public boolean isFarmaceut() {
		return farmaceut;
	}

	public void setFarmaceut(boolean farmaceut) {
		this.farmaceut = farmaceut;
	}
	
	public boolean isVarkforer(){
		return varkforer;
	}
	
	public void setVarkforer(boolean varkforer){
		this.varkforer = varkforer;	
	}

	public int getOprId() { 
		return oprId; 
	}

	public void setOprId(int oprId) { 
		this.oprId = oprId; 
	}

	public String getIni() { 
		return ini; 
	}

	public void setIni(String ini) { 
		this.ini = ini; 
	}

	public String getCpr() { 
		return cpr; 
	}

	public void setCpr(String cpr) { 
		this.cpr = cpr; 
	}

	public String getPassword() { 
		return password; 
	}

	public void setPassword(String password) { 
		this.password = password; 
	}

	public String toString() { 
		return oprId + "\t" + navn + "\t" + ini + "\t" + cpr + "\t" + password; 
	}
}
