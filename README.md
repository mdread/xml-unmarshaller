XML-Unmarshaller
================

Download and compile
-------
clone the git repo and use [Maven](http://maven.apache.org/ "Maven") to compile and build a jar
```bash
git clone https://github.com/0xcaos/xml-unmarshaller.git
cd xml-unmarshaller
mvn package
```
the generated jar is in the **./target** directory

or install it locally with *mvn install* and add the dependency in your pom.xml
```xml
<dependency>
	<groupId>net.caoticode.unmarshaller</groupId>
	<artifactId>xml-unmarshaller</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

Usage
-----
as an example lets say you have the following xml
```xml
<user>
  <name>daniel</name>
  <age>25</age>
  <groups>
    <group>user</group>
    <group>editor</group>
    <group>moderator</group>
  </groups>
</user>
```
here is the java bean ready to be mapped with the xml

```java
@XPathPrefix("/user")
class User {
  @XPath("/name/text()")
  private String name;

  @XPath("/age/text()")
  private Integer age;

  @XPath("/groups/group/text()")
  private List<String> groups;

  // ... and the usual getters and setters ...
}
```

and to unmarshall

```java
class Test {
  public static void main(String[] args){
    XMLUnmarshaller unmarshaller = new XMLUnmarshaller(User.class);
    User user = unmarshaller.unmarshall(new FileReader("person.xml"));
    System.out.println("Hi " + user.getName() + "!");
  }
}
```

The unmarshaller dosn't really know how to parse every kind of data, it uses *ValueParsers* to trasform a string from the xml to the expected data type;
it has ValueParsers for the base types like *String*, *Boolean*, *Integer*, *Double*, *Float* and *Date* (with the default DateFormat) but you can specify your own parsers implementing the **ValueParser** interface

here is how the DateParser is implemented:
```java
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
```

once implemented register it to the unmarshaller
```java
unmarshaller.registerParser(new DateParser("dd/MM/yyyy"))
```

and now all Date objects in your bean would be parsed using **dd/MM/yyyy** format ;)

other option is to specify the parser to use directly in the annotated bean (for example to override a registered parser just for a specific field)
```java
@XPath("/created/text()", paser=DateParser.class, parserParams={"yyMMddHHmmssZ"})
private Date registrationDate;
```
