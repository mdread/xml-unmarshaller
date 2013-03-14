package net.caoticode.unmarshaller;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public interface ValueParser<T> {
	public T parse(String value) throws ParseException;
}
