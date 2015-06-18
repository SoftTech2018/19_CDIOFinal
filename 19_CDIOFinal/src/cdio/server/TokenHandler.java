package cdio.server;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TokenHandler {
	private static TokenHandler instance;
	private static final int TOKEN_LIFETIME = 30; //in minuttes.
	private final static String KEY = "Summer petrichor"; //16 chars = 16 byte = 128 bit
	private final static String SIGNATURE = "Tessaract, the four dimensional equivalent of a cube."; 
	private final static String SIGNATURE_KEY = "Tetradecahedrons";
	
	
	public static TokenHandler getInstance(){
		if(instance == null) instance = new TokenHandler();
		return instance;
	}

	
	public String createToken(String user_id){
		if(user_id.contains("\n")) throw new IllegalArgumentException("User Id cannot contain line breaks!");
		String signature = encrypt(SIGNATURE, SIGNATURE_KEY);
		String timestamp = prettyTime();
		String unixTime = String.valueOf(System.currentTimeMillis());
		String token = signature+"\n"+user_id+"\n"+timestamp+"\n"+unixTime;
		return encrypt(token);
	}
	public String validateToken(String token){
		try {
			String _data = decrypt(token);
			String[] data = _data.split("\n");
			String actual_signature = decrypt(data[0], SIGNATURE_KEY);
			if(!SIGNATURE.equals(actual_signature)) {
				System.err.println("Token signature doesn't match");
				return null;
			}

			String _unixTime = data[3];
			long unixTime = Long.parseLong(_unixTime);
			if(unixTime + toMillis(TOKEN_LIFETIME, TimeUnit.MINUTES) > System.currentTimeMillis()){
				data[2] = prettyTime();
				data[3] = String.valueOf(System.currentTimeMillis());
				StringBuilder sb = new StringBuilder();
				for(String s : data){ sb.append(s+"\n"); }
				return sb.toString();
			} else {
				System.err.println("Token timed out");
				return null;
			}
		} catch(Exception e) {
			return null;
		}
	}
	 
	public String getUserID(String token){
		try {
			String _data = decrypt(token);
			String[] data = _data.split("\n");
			return data[1];
		} catch (Exception e) {
			return null;
		}
	}
	public String getTimestamp(String token){
		try {
			String _data = decrypt(token);
			String[] data = _data.split("\n");
			return data[2];
		} catch (Exception e) {
			return null;
		}
	}
	public String getUnixTime(String token){
		try {
			String _data = decrypt(token);
			String[] data = _data.split("\n");
			return data[3];
		} catch (Exception e) {
			return null;
		}
	}
		
	public String encrypt(String token) { return encrypt(token, KEY); }
	public String encrypt(String token, String key) {
		try {
			Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());

			cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivParameterSpec);
			byte[] encrypted = cipher.doFinal(token.getBytes("UTF-8"));
			
			Base64.Encoder encoder = Base64.getEncoder();
			return encoder.encodeToString(encrypted);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException 
				| BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String decrypt(String encrypted) { return decrypt(encrypted, KEY); } 
	public String decrypt(String encrypted, String key) {
		try {
			Key aesKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());;
			
			Base64.Decoder decoder = Base64.getDecoder();
			cipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);
			String token = new String(cipher.doFinal(decoder.decode(encrypted)));
			return token;

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException 
				| BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String prettyTime(){ return prettyTime(System.currentTimeMillis()); }
	private String prettyTime(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		int sec = cal.get(Calendar.SECOND);
		String _sec = String.format("%02d", sec);
		int min = cal.get(Calendar.MINUTE);
		String _min = String.format("%02d", min);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String _hour = String.format("%02d", hour);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String _day = String.format("%02d", day);
		int month = cal.get(Calendar.MONTH) +1;
		String _month = String.format("%02d", month);
		int year = cal.get(Calendar.YEAR);
		String _year = Integer.toString(year);
		String time = _hour+":"+_min+":"+_sec+" "+_day+"/"+_month+"-"+_year;
		return time;
	}

	private int toMillis(int value, TimeUnit unit){
		switch(unit){
		case DAYS: value *= 24;
		case HOURS: value *= 60;
		case MINUTES: value *= 60;
		case SECONDS: value *= 1000;
		case MILLISECONDS: return value;
		case MICROSECONDS: throw new UnsupportedOperationException("Microseconds is not supported");
		case NANOSECONDS: throw new UnsupportedOperationException("Nanoseconds is not supported");
		default: throw new UnsupportedOperationException("The timeunit "+unit+" is not supported"); 
		}
	}


}