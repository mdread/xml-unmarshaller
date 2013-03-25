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
the generated jar is under the **./target** directory

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
