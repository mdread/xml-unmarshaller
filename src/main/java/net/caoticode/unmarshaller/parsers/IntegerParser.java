package net.caoticode.unmarshaller.parsers;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class IntegerParser implements ValueParser<Integer> {

	public Integer parse(String value) throws ParseException {
		return Integer.parseInt(value);
	}

}
