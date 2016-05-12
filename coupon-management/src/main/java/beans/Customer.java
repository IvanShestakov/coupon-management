package beans;

import java.util.Collection;

public class Customer {
	private long id;
	private String CustName;
	private String password;
	private Collection<Coupon> coupons;
	
	public Customer(){
		//Default constructor
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return CustName;
	}

	public void setCustName(String custName) {
		CustName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}
	
	@Override
	public String toString(){
		String result = "|Customer ID: " + this.id + " |Customer Name: " + this.CustName + " |Password: "+ this.password;
		return result;
	}

}
