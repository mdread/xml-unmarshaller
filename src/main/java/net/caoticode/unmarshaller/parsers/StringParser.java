package net.caoticode.unmarshaller.parsers;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class StringParser implements ValueParser<String> {

	public String parse(String value) throws ParseException {
		return value;
	}

}
