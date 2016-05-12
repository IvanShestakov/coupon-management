package beans;

import java.util.Collection;

public class Company {
	private long id;
	private String CompName;
	private String password;
	private String email;
	private Collection<Coupon> coupons;
	
	public Company(){
	} 
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getCompName() {
		return CompName;
	}

	public void setCompName(String compName) {
		CompName = compName;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Collection<Coupon> getCoupons() {
		return coupons;
	}


	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}


	public String toString(){
		String result =  "|Company ID: " + this.id + "|Name: " + this.CompName + "|Email: " + this.email + "|Password: " + this.password;
		
		return result;
	}
	
	
	
}
