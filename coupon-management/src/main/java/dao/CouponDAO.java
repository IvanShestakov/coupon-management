package dao;

import java.util.Collection;

import beans.Coupon;
import beans.CouponType;
import exceptions.ApplicationException;

public interface CouponDAO {

	public long createCoupon(Coupon coupon, Long companyId) throws ApplicationException;
	public void removeCoupon(long id) throws ApplicationException;
	public void updateCoupon(Coupon coupon) throws ApplicationException;
	public Coupon getCoupon(long id) throws ApplicationException;
	public Collection<Coupon> getAllCoupons() throws ApplicationException;
	public Collection<Coupon> getCouponByType(CouponType coupontype) throws ApplicationException;
	public void purchaseCouponForCustomer(long couponId, long custId) throws ApplicationException;
	public void removeCouponForCustomer(long couponId, long customerId ) throws ApplicationException;
	
}
