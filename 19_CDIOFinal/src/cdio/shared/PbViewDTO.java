package cdio.shared;

import java.io.Serializable;

public class PbViewDTO implements Serializable{
	
	private String raavareNavn;
	private int opr, terminal, batch, raavareId, status;
	private double maengde, tara, netto, tolerance;
	
	public PbViewDTO(){
		
	}
	
	public PbViewDTO(String raavareNavn, int opr, int terminal, int batch, double maengde, double tara, double netto, double tolerance){
		this.raavareNavn = raavareNavn;
		this.opr = opr;
		this.terminal = terminal;
		this.batch = batch;
		this.maengde = maengde;
		this.tara = tara;
		this.netto = netto;
		this.tolerance = tolerance;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setStatus(int id){
		status = id;
	}
	
	public int getRaavareId(){
		return raavareId;
	}
	
	public void setRaavareId(int id){
		raavareId = id;
	}

	public String getRaavareNavn() {
		return raavareNavn;
	}

	public void setRaavareNavn(String raavareNavn) {
		this.raavareNavn = raavareNavn;
	}

	public int getOpr() {
		return opr;
	}

	public void setOpr(int opr) {
		this.opr = opr;
	}

	public int getTerminal() {
		return terminal;
	}

	public void setTerminal(int terminal) {
		this.terminal = terminal;
	}

	public int getBatch() {
		return batch;
	}

	public void setBatch(int batch) {
		this.batch = batch;
	}

	public double getMaengde() {
		return maengde;
	}

	public void setMaengde(double maengde) {
		this.maengde = maengde;
	}

	public double getTara() {
		return tara;
	}

	public void setTara(double tara) {
		this.tara = tara;
	}

	public double getNetto() {
		return netto;
	}

	public void setNetto(double netto) {
		this.netto = netto;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
	
	
	

}
