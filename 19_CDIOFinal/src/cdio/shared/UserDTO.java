package cdio.shared;

import java.io.Serializable;

public class UserDTO implements Serializable{
	
	private int userId;
	private String password, name, cpr, ini;
	private boolean admin, farmaceut, vaerkfoerer, operatoer;
	
	public UserDTO(){
	}
	
	public UserDTO(String userId, String password) {
		this.userId = Integer.parseInt(userId);
		this.password = password;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCpr() {
		return cpr;
	}
	public void setCpr(String cpr) {
		this.cpr = cpr;
	}
	public String getIni() {
		return ini;
	}
	public void setIni(String ini) {
		this.ini = ini;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isFarmaceut() {
		return farmaceut;
	}
	public void setFarmaceut(boolean farmaceut) {
		this.farmaceut = farmaceut;
	}
	public boolean isVaerkfoerer() {
		return vaerkfoerer;
	}
	public void setVaerkfoerer(boolean vaerkfoerer) {
		this.vaerkfoerer = vaerkfoerer;
	}
	public boolean isOperatoer() {
		return operatoer;
	}
	public void setOperatoer(boolean operatoer) {
		this.operatoer = operatoer;
	}
}
