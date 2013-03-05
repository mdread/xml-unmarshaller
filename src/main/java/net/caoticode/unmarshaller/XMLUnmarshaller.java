package net.caoticode.unmarshaller;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import net.caoticode.unmarshaller.annotation.XPath;
import net.caoticode.unmarshaller.annotation.XPathPrefix;
import net.caoticode.unmarshaller.util.XMLUtil;

public class XMLUnmarshaller {
	
	private static Class<?>[] SIMPLE_TYPES = new Class<?>[]{String.class, Integer.class, Double.class, Float.class, Boolean.class};
	
	private Class<?> type;
	private List<Tuple<Field, XPath>> classMetadata;
	String xpathPrefix;
	
	public XMLUnmarshaller(Class<?> type) {
		this.type = type;
		this.classMetadata = extractAnnotations();
		this.xpathPrefix = extractPrefix();
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
	
	private Object getValueForField(Field field, XPath annotation, XMLUtil xml){
		String xpath = xpathPrefix + annotation.value();
		Class<?> fieldType = field.getType();
		
		if(isSimpleType(fieldType)){
			return getSimpleValue(field, xpath, xml);
		} else if(List.class.isAssignableFrom(fieldType)) {
			return getListValue(field, xpath, xml);
		} else {
			return null;
		}
		
	}
	
	private Object getSimpleValue(Field field, String xpath, XMLUtil xml){
		Class<?> fieldType = field.getType();
		String val = xml.read(xpath);
		return parseSimpleType(fieldType, val);
	}
	
	private List<? extends Object> getListValue(Field field, String xpath, XMLUtil xml){
		List<Object> result = new LinkedList<Object>();
		ParameterizedType parameterizedType = (ParameterizedType)field.getGenericType();
		Class<?> genericType = (Class<?>)parameterizedType.getActualTypeArguments()[0];
		
		if(isSimpleType(genericType)) {
			for (String val : xml.readList(xpath)) {
				result.add(parseSimpleType(genericType, val));
			}
		} else {
			// TODO implement
		}
		return result;
	}
	
	private Object parseSimpleType(Class<?> fieldType, String val){
		try{
			if(String.class.isAssignableFrom(fieldType)){
				return val;
			} else if (Integer.class.isAssignableFrom(fieldType)) {
				return Integer.parseInt(val);
			} else if (Double.class.isAssignableFrom(fieldType)) {
				return Double.parseDouble(val);
			} else if (Float.class.isAssignableFrom(fieldType)) {
				return Float.parseFloat(val);
			} else if (Boolean.class.isAssignableFrom(fieldType)) {
				return Boolean.parseBoolean(val);
			} else {
				return null;
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isSimpleType(Class<?> type){
		for (Class<?> simpleType : SIMPLE_TYPES) {
			if(simpleType.isAssignableFrom(type)) return true;
		}
		
		return false;
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
