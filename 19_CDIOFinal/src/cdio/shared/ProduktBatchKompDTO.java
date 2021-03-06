package cdio.shared;

import java.io.Serializable;

public class ProduktBatchKompDTO implements Serializable 
{
	private int pbId; 	  // produktbatchets id
	private int rbId;        // i omraadet 1-99999999
	private double tara;
	private double netto;
	private int oprId;					// operatoer-nummer
	private String host;

	
	public ProduktBatchKompDTO(int pbId, int rbId, double tara, double netto, int oprId, String host)
	{
		this.pbId = pbId;
		this.rbId = rbId;
		this.tara = tara;
		this.netto = netto;
		this.oprId = oprId;
		this.host = host;
	}
	
	public ProduktBatchKompDTO(){
		
	}
	
	public int getPbId() { return pbId; }
	public void setPbId(int pbId) { this.pbId = pbId; }
	public int getRbId() { return rbId; }
	public void setRbId(int rbId) { this.rbId = rbId; }
	public double getTara() { return tara; }
	public void setTara(double tara) { this.tara = tara; }
	public double getNetto() { return netto; }
	public void setNetto(double netto) { this.netto = netto; }
	public int getOprId() { return oprId; }
	public void setOprId(int oprId) { this.oprId = oprId; }
	public String getHost() { return host; }
	public void setHost(String host) { this.host = host; }
	public String toString() { 
		return pbId + "\t" + rbId +"\t" + tara +"\t" + netto + "\t" + oprId ; 
	}
}
