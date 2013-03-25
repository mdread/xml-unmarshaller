package net.caoticode.unmarshaller.parsers;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class BooleanParser implements ValueParser<Boolean> {
	private static final String[] TRUE_STRINGS = {"true", "yes", "1"};
	
	public Boolean parse(String value) throws ParseException {
		for (String ts : TRUE_STRINGS)
			if(value.trim().toLowerCase().equals(ts))
				return true;
		
		return false;
	}

}
