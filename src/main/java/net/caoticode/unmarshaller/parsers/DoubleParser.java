package net.caoticode.unmarshaller.parsers;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class DoubleParser implements ValueParser<Double> {

	public Double parse(String value) throws ParseException {
		return Double.parseDouble(value);
	}

}
