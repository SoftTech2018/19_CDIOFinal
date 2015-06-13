package cdio.shared;

import java.io.Serializable;

public class TokenException extends Exception implements Serializable{

	public TokenException(String message) { super(message); }    
	public TokenException(Exception e) { super(e); }
	public TokenException(){};
}
