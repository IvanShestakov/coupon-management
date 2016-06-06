package facades;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

import beans.Coupon;
import beans.CouponType;
import beans.Customer;
import dao.CouponDAO;
import dao.CustomerDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;

public class CustomerFacade implements CouponClientFacade{
	private CustomerDAO customerdao;
	private CouponDAO coupondao;
	
	public CustomerFacade(){
		customerdao = new CustomerDBDAO();
		coupondao = new CouponDBDAO();
	}
	
	@Override
	public CouponClientFacade login(String login, String password, ClientType clientType) throws ApplicationException {
		// Verify that customer with this name exists
				if (customerdao.isCustomerNameExists(login)) {
					// Validate that name and password provided match those in the
					// database.
					if (password.equals(customerdao.getCustomerByName(login).getPassword())
							&& clientType.equals(ClientType.CUSTOMER)) {
						return new CustomerFacade();
					}
				}
				return null;
	}
	//TODO refactor the method to return Coupon instance.
	public void purchaseCouponForCustomer(long couponId, long customerId) throws ApplicationException{
		//Validate that coupon with provided id exists, it's expiration date has not passed yet and it's amount is greater than 0.
		//Validate that customer with provided id exists and doesn't already have the requested coupon. 
		Coupon coupon = coupondao.getCoupon(couponId);
		Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		if (coupon.getEndDate().before(today)){
			throw new ApplicationException(ErrorType.COUPON_ALREADY_EXPIRED);
		}
		int couponAmount = coupon.getAmount();
		if (couponAmount == 0){
			throw new ApplicationException(ErrorType.COUPON_AMOUNT_IS_ZERO);
		}
		Customer customer = customerdao.getCustomer(customerId);
		
		Collection<Coupon> coupons = customerdao.getCoupons(customerId);
		for (Coupon c : coupons){
			if (couponId == c.getId()){
				throw new ApplicationException(ErrorType.COUPON_ALREADY_OWNED_BY_CUSTOMER);
			}
		}
		
		if (customer!=null && coupon!=null && couponAmount>0){
			//Record the coupon purchase in the db
			coupondao.purchaseCouponForCustomer(couponId, customerId);
			//Update the amount of available CouponResource.
			coupon.setAmount(couponAmount-1);
			coupondao.updateCoupon(coupon);
		}
	}
	public Collection<Coupon> getAllPurchasedCoupons(long customerId) throws ApplicationException{
		//Retrieve all customer CouponResource and return as the results
		Collection<Coupon> customercoupons = null;
		if (customerdao.isCustomerWithIdExists(customerId)) {
			customercoupons = customerdao.getCoupons(customerId);
		}
		
		return customercoupons;
	}
	
	public Collection<Coupon> getPurchasedCouponsByType(long customerId, CouponType type) throws ApplicationException{
		//Create empty result collection of CouponResource
		Collection<Coupon> result = new HashSet<Coupon>();
		//Retrieve all customer CouponResource
		Collection<Coupon> customercoupons = getAllPurchasedCoupons(customerId);
		
		//Iterate over CouponResource and add CouponResource of correct type to result
		for (Coupon c : customercoupons){
			if (c.getType().equals(type)){
				result.add(c);
			}
		}
		//Return the resulting set
		return result;
	}
	
	public Collection<Coupon> getPurchasedCouponsByPrice(long customerId, double price) throws ApplicationException{
		//Create empty result collection of CouponResource
		Collection<Coupon> result = new HashSet<Coupon>();
		//Retrieve all customer CouponResource
		Collection<Coupon> customercoupons = getAllPurchasedCoupons(customerId);
		
		//Iterate over CouponResource and add CouponResource to result if their price is lower than requested
		for (Coupon c : customercoupons){
			if (c.getPrice() < price){
				result.add(c);
			}
		}
		//Return the resulting set
		return result;
	}
	
}
