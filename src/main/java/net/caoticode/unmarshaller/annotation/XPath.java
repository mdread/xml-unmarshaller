package net.caoticode.unmarshaller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.caoticode.unmarshaller.ParseException;
import net.caoticode.unmarshaller.ValueParser;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XPath {
	String value();
	Class<? extends ValueParser<?>> parser() default None.class;
	String[] parserParams() default {};
	
	public static class None implements ValueParser<Object>{
		public Object parse(String value) throws ParseException {
			throw new RuntimeException("nothing to do here...");
		}
	}
}
