package facades;

import java.util.Collection;

import beans.Coupon;
import dbdao.CouponDBDAO;
import exceptions.ApplicationException;

public class CouponFacade {

	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		// Fetch all the Coupons from the persistence layer
		CouponDBDAO coupondao = new CouponDBDAO();

		Collection<Coupon> allcoupons = coupondao.getAllCoupons();
		return allcoupons;
	}
}
