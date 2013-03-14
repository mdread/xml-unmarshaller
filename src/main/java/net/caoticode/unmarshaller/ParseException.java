package net.caoticode.unmarshaller;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class ParseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
