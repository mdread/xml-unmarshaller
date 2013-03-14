package net.caoticode.unmarshaller;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import net.caoticode.unmarshaller.parsers.DateParser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AppTest extends TestCase {
	String xml = "<openDamsUserWSResponse><active>true</active><addresses><address>via dell'arco del monte</address><address>via beniamino de ritis</address><address>via monte zebio</address></addresses><date>01/12/1987</date><companies><acronym>openDams</acronym><companyName>Amministratore di Sistema</companyName><idCompany>1000000000</idCompany></companies><idUser>1</idUser><password>2c40837dd4b97fd00a5a598a8a25426f</password><roles><idRole>4</idRole><roleName>ROLE_READER</roleName></roles><username>sandro</username><usersProfile><numbers><number>8</number><number>23</number><number>42</number></numbers><birthDate>1976-01-26T00:00:00+01:00</birthDate><email>0xcaos@gmail.com</email><language>IT</language><lastname>Camarda</lastname><name>Daniel</name><telephoneNumber>3921168694</telephoneNumber></usersProfile></openDamsUserWSResponse>";
	XMLUnmarshaller userUnmarshaller = new XMLUnmarshaller(User.class);
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) {
        super( testName );
        userUnmarshaller.registerParser(new DateParser("dd/MM/yyyy"));
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite( AppTest.class );
    }

    public void testUnmarshallUser() {
    	User usr = userUnmarshaller.unmarshall(new StringReader(xml));
    	assertNotNull(usr);
    }
    
    public void testSimpleTypeUnmarshalling() {
    	User usr = userUnmarshaller.unmarshall(new StringReader(xml));
    	assertEquals(Integer.valueOf(1), usr.getId());
    	assertEquals("Daniel", usr.getName());
    }
    
    public void testListUnmarshalling() {
    	User usr = userUnmarshaller.unmarshall(new StringReader(xml));
    	List<Integer> numbers = new LinkedList<Integer>();
    	numbers.add(8);
    	numbers.add(23);
    	numbers.add(42);
    	assertEquals(numbers, usr.getLuckyNumbers());
    }
    
    public void testValueParser() {
    	User usr = userUnmarshaller.unmarshall(new StringReader(xml));
    	assertNotNull(usr.getBirthDate());
    }
}
