package net.caoticode.unmarshaller;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class UnmarshallException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public UnmarshallException(String message, Throwable cause) {
		super(message, cause);
	}
}
