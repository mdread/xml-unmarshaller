package net.caoticode.unmarshaller.util;

import java.io.Reader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * @author Daniel Camarda (0xcaos@gmail.com)
 *
 */
public class XMLUtil {
	private Document xml = null;
	XPath xPath = null;
	
	public XMLUtil(Reader source) {
		try{
			DocumentBuilderFactory builderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderfactory.newDocumentBuilder();
			xml = builder.parse(new InputSource(source));
			
			XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
			xPath = factory.newXPath();
		} catch(Exception e) {
			throw new RuntimeException("error parsing xml", e.getCause());
		}
		
	}
	
	public String read(String xpath){
		try {
			XPathExpression expr = xPath.compile(xpath);
			return expr.evaluate(xml, XPathConstants.STRING).toString();
		} catch (XPathExpressionException e) {
			throw new RuntimeException("error compiling xpath expression", e.getCause());
		}
	}
	
	public List<String> readList(String xpath){
		List<String> result = new LinkedList<String>();
		try {
			XPathExpression expr = xPath.compile(xpath);
			NodeList nodes =  (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				result.add(nodes.item(i).getTextContent());
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException("error compiling xpath expression", e.getCause());
		}
		
		return result;
	}
	
	public List<String> readXMLList(String xpath){
		List<String> result = new LinkedList<String>();
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
	        Transformer transformer = tFactory.newTransformer();
	        transformer.setOutputProperty("indent", "yes");
	        
			XPathExpression expr = xPath.compile(xpath);
			NodeList nodes =  (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				StringWriter sw = new StringWriter();
				StreamResult xmlResult = new StreamResult(sw);
				transformer.transform(new DOMSource(nodes.item(i)), xmlResult);
				result.add(sw.toString());
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException("error compiling xpath expression", e.getCause());
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
}
