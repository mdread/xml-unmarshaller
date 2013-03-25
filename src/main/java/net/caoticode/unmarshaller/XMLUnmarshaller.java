package net.caoticode.unmarshaller;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.caoticode.unmarshaller.annotation.XPath;
import net.caoticode.unmarshaller.annotation.XPathPrefix;
import net.caoticode.unmarshaller.parsers.BooleanParser;
import net.caoticode.unmarshaller.parsers.DateParser;
import net.caoticode.unmarshaller.parsers.DoubleParser;
import net.caoticode.unmarshaller.parsers.FloatParser;
import net.caoticode.unmarshaller.parsers.IntegerParser;
import net.caoticode.unmarshaller.parsers.StringParser;
import net.caoticode.unmarshaller.util.XMLUtil;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class XMLUnmarshaller {
	private Class<?> type;
	private List<Tuple<Field, XPath>> classMetadata;
	String xpathPrefix;
	Map<Class<?>, ValueParser<?>> parsers = new HashMap<Class<?>, ValueParser<?>>();
	
	public XMLUnmarshaller(Class<?> type) {
		this.type = type;
		this.classMetadata = extractAnnotations();
		this.xpathPrefix = extractPrefix();
		
		// register default parsers
		registerParser(new StringParser());
		registerParser(new IntegerParser());
		registerParser(new DoubleParser());
		registerParser(new FloatParser());
		registerParser(new BooleanParser());
		registerParser(new DateParser());
	}

	@SuppressWarnings("unchecked")
	public <T> T unmarshall(Reader xmlSource) {
		try {
			XMLUtil xml = new XMLUtil(xmlSource);
			T result = (T)getInstance();
			
			for (Tuple<Field, XPath> annotatedFields : classMetadata) {
				Object value = getValueForField(annotatedFields.first, annotatedFields.second, xml);
				setValue(result, annotatedFields.first, value);
			}
			
			return result;
		} catch (Exception e) {
			throw new UnmarshallException(e.getMessage(), e);
		} 
	}
	
	public void registerParser(ValueParser<?> parser){
		boolean canAddParser = false;
		Type[] genInterfaces = parser.getClass().getGenericInterfaces();
		for (Type type : genInterfaces) {
			if(type instanceof ParameterizedType){
				ParameterizedType ptype = (ParameterizedType)type;
				if(((Class<?>)ptype.getRawType()).equals(ValueParser.class)){
					parsers.put((Class<?>)ptype.getActualTypeArguments()[0], parser);
					canAddParser = true;
					break;
				}
			}
		}
		
		if(!canAddParser)
			throw new RuntimeException("generic parameter missing in ValueParser implementation");
	}
	
	private Object getValueForField(Field field, XPath annotation, XMLUtil xml) throws InstantiationException, IllegalAccessException, ParseException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		String xpath = xpathPrefix + annotation.value();
		Class<? extends ValueParser<?>> parserClass = annotation.parser();
		String[] parserParams = annotation.parserParams();
		Class<?> fieldType = field.getType();
		
		if(!parserClass.equals(XPath.None.class)){
			return instanciateParser(parserClass, parserParams).parse(xml.read(xpath));
		} else if(hasParser(fieldType)){
			return getParser(fieldType).parse(xml.read(xpath));
		} else if(List.class.isAssignableFrom(fieldType)) {
			return getListValue(field, xpath, xml);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private ValueParser<?> instanciateParser(Class<? extends ValueParser<?>> parserClass, String[] parserParams) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(parserParams.length == 0){
			return parserClass.newInstance();
		} else {
			Class<String>[] paramsClass = new Class[parserParams.length];
			for (int i = 0; i < parserParams.length; i++) {
				paramsClass[i] = String.class;
			}
			return parserClass.getConstructor(paramsClass).newInstance((Object[])parserParams);
		}
		
	}
	
//	private boolean hasParser(Class<?> type){
//		for (Class<?> parserType : parsers.keySet()) {
//			if(parserType.isAssignableFrom(type))
//				return true;
//		}
//		return false;
//	}
	
	private boolean hasParser(Class<?> type){
		return parsers.containsKey(type);
	}
	
//	private ValueParser<?> getParser(Class<?> type){
//		for (Class<?> parserType : parsers.keySet()) {
//			if(parserType.isAssignableFrom(type))
//				return parsers.get(parserType);
//		}
//		return null;
//	}
	
	private ValueParser<?> getParser(Class<?> type){
		return parsers.get(type);
	}
	
	private List<? extends Object> getListValue(Field field, String xpath, XMLUtil xml){
		List<Object> result = new LinkedList<Object>();
		ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
		Class<?> genericType = (Class<?>)parameterizedType.getActualTypeArguments()[0];
		
		if(hasParser(genericType)) {
			for (String val : xml.readList(xpath)) {
				result.add(getParser(genericType).parse(val));
			}
		} else {
			XMLUnmarshaller um = new XMLUnmarshaller(genericType);
			for (String val : xml.readXMLList(xpath)) {
				result.add(um.unmarshall(new StringReader(val)));
			}
		}
		return result;
	}

	private List<Tuple<Field, XPath>> extractAnnotations(){
		List<Tuple<Field, XPath>> annotations = new LinkedList<XMLUnmarshaller.Tuple<Field,XPath>>();
		for (Field field : type.getDeclaredFields()) {
			field.setAccessible(true);
			XPath ann = field.getAnnotation(XPath.class);
			if(ann != null){
				annotations.add(new Tuple<Field, XPath>(field, ann));
			}
		}
		
		return annotations;
	}
	
	private String extractPrefix() {
		XPathPrefix ann = type.getAnnotation(XPathPrefix.class);
		if(ann != null){
			return ann.value();
		} else {
			return "";
		}
	}
	
	private Object getInstance() throws InstantiationException, IllegalAccessException{
		return type.newInstance();
	}
	
	private void setValue(Object instance, Field field, Object value) throws IllegalArgumentException, IllegalAccessException{
		field.set(instance, value);
	}
	
	private static class Tuple<K, S>{
		public final K first;
		public final S second;
		
		public Tuple(K first, S second) {
			this.first = first;
			this.second = second;
		}
	}
}
