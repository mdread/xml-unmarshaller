package net.caoticode.unmarshaller.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class DateParser implements ValueParser<Date> {
	SimpleDateFormat formatter = null;
	
	public DateParser() {
		formatter = new SimpleDateFormat();
	}
	
	public DateParser(String pattern) {
		formatter = new SimpleDateFormat(pattern);
	}
	
	public Date parse(String value) {
		try {
			return formatter.parse(value);
		} catch (ParseException e) {
			throw new net.caoticode.unmarshaller.ParseException(e.getMessage(), e.getCause());
		}
	}

}
