package net.caoticode.unmarshaller.parsers;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class FloatParser implements ValueParser<Float> {

	public Float parse(String value) throws ParseException {
		return Float.parseFloat(value);
	}

}
