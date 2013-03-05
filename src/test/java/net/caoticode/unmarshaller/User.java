package net.caoticode.unmarshaller;

import java.util.List;

import net.caoticode.unmarshaller.annotation.XPath;
import net.caoticode.unmarshaller.annotation.XPathPrefix;

@XPathPrefix("/openDamsUserWSResponse")
public class User {
	@XPath("/idUser/text()")
	private Integer id;
	
	@XPath("/usersProfile/name/text()")
	private String name;
	
	@XPath("/password/text()")
	private String password;
	
	@XPath("/addresses/address/text()")
	private List<String> addresses;
	
	@XPath("//number/text()")
	private List<Integer> luckyNumbers;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	public List<Integer> getLuckyNumbers() {
		return luckyNumbers;
	}
	public void setLuckyNumbers(List<Integer> luckyNumbers) {
		this.luckyNumbers = luckyNumbers;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password="
				+ password + ", addresses=" + addresses + ", luckyNumbers="
				+ luckyNumbers + "]";
	}
}