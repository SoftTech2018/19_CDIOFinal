package cdio.shared;

import java.io.Serializable;

public class PbViewDTO implements Serializable{
	
	private String raavareNavn, oprIni, terminal;
	private int batch, raavareId;
	private double maengde, tara, netto, tolerance;
	
	public PbViewDTO(){
		
	}
	
	public PbViewDTO(String raavareNavn, int id, String opr, int batch, double maengde, double tara, double netto, double tolerance, String terminal){
		this.raavareNavn = raavareNavn;
		this.oprIni = opr;
		this.batch = batch;
		this.maengde = maengde;
		this.tara = tara;
		this.netto = netto;
		this.tolerance = tolerance;
		this.raavareId = id;
		this.terminal = terminal;
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

	public String getOpr() {
		return oprIni;
	}

	public void setOpr(String opr) {
		this.oprIni = opr;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
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
