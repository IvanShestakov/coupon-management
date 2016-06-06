package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	private String username;
	private String password;
	private facades.ClientType type;
	
	public User(){
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public facades.ClientType getType() {
		return type;
	}

	public void setType(facades.ClientType type) {
		this.type = type;
	}
	
}
