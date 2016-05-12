package dao;

import java.util.Collection;

import beans.Coupon;
import beans.Customer;
import exceptions.ApplicationException;

public interface CustomerDAO {
	public long createCustomer(Customer customer) throws ApplicationException;
	public void removeCustomer(long id) throws ApplicationException;
	public void updateCustomer(Customer customer) throws ApplicationException;
	public Customer getCustomer(long id) throws ApplicationException;
	public Customer getCustomerByName(String cust_name) throws ApplicationException;
	public Collection<Customer> getAllCustomers() throws ApplicationException;
	public Collection<Coupon> getCoupons(long id) throws ApplicationException;
	public boolean login(String custName, String password) throws ApplicationException;
	public Collection<Long> getCustomerIdByCouponId(long coupon_id) throws ApplicationException;
	public boolean isCustomerNameExists(String cust_name) throws ApplicationException;
	boolean isCustomerWithIdExists(Long customerId) throws ApplicationException;
	}
