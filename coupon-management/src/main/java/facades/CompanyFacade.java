package facades;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;

import beans.Coupon;
import beans.CouponType;
import dao.CompanyDAO;
import dao.CouponDAO;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;

public class CompanyFacade implements CouponClientFacade {

	private CompanyDAO companydao;
	private CouponDAO coupondao;

	@Override
	public CouponClientFacade login(String login, String password, ClientType clientType) throws ApplicationException {
		// Verify that company with this name exists
		try {
		if (companydao.getCompanyByName(login) != null) {
			// Validate that name and password provided match those in the
			// database.
			if (password.equals(companydao.getCompanyByName(login).getPassword())
					&& clientType.equals(ClientType.COMPANY)) {
				return new CompanyFacade();
			}
			else {
				throw new ApplicationException(ErrorType.FAILED_TO_LOGIN);
			}
		}
		}
		catch (ApplicationException e){
		throw new ApplicationException(ErrorType.FAILED_TO_LOGIN, e);
		}
		return null;
	}

	public CompanyFacade() {
		companydao = new CompanyDBDAO();
		coupondao = new CouponDBDAO();
	}

	public Long CreateCoupon(Coupon coupon, Long comp_id) throws ApplicationException {
		// Check that provided company id and the coupon are valid.
		if (companydao.getCompanyById(comp_id) != null && coupon != null) {
			// Create the coupon and if the create operation didn't result in
			// -1, return the id of newly created coupon.
			Long coupon_id = coupondao.createCoupon(coupon, comp_id);
			if (coupon_id != -1) {
				return coupon_id;
			}
		}
		throw new ApplicationException(ErrorType.FAILED_TO_CREATE_COUPON);

	}

	public void RemoveCoupon(Coupon coupon) throws ApplicationException {
		// Check that the coupon provided is not null
		if (coupon != null) {
			// Remove the coupon and all it's occurrences in the database.
			coupondao.removeCoupon(coupon.getId());
		}
	}

	public void UpdateCoupon(Coupon coupon) throws ApplicationException {
		//Check that the coupon provided is not null and coupon with this id exists
		if (coupon != null) {
			coupondao.updateCoupon(coupon);
		}
		else {
			throw new ApplicationException(ErrorType.COUPON_DETAILS_ARE_INVALID);
		}
	}

	public Coupon getCoupon(Long id) throws ApplicationException {
		Coupon coupon = null;
		try {
			coupon = coupondao.getCoupon(id);
			return coupon;
		} catch (ApplicationException e) {
			throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB, e);
		}

	}

	public Collection<Coupon> getAllCoupons(long comp_id) throws ApplicationException {
		// Check whether company with this id exists
		if (companydao.getCompanyById(comp_id) != null) {
			// Return all company coupons
			return companydao.getCoupons(comp_id);
		} else {
			throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
		}
	}

	public Collection<Coupon> getCouponByType(long comp_id, CouponType type) throws ApplicationException {
		// Check whether company with this id exists
				if (companydao.getCompanyById(comp_id)!=null) {
					// Get all company coupons
					Collection<Coupon> allcoupons = companydao.getCoupons(comp_id);
					//
					Collection<Coupon> results = new HashSet<Coupon>();
					// Iterate over all coupons and add those which have the correct type to results
					for (Coupon c : allcoupons){
						if (c.getType().equals(type)){
							results.add(c);
						}
					}
					return results;
				}
				else {
					throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
				}
				
	}
	public Collection<Coupon> getCouponsCheaperThan(long comp_id, double coupon_price) throws ApplicationException {
		// Check whether company with this id exists
		if (companydao.getCompanyById(comp_id)!=null) {
			// Get all company coupons
			Collection<Coupon> allcoupons = companydao.getCoupons(comp_id);
			// Create temporary storage for results
			Collection<Coupon> results = new HashSet<Coupon>();
			// Iterate over all coupons and add those which have the price lower than required to results
			for (Coupon c : allcoupons){
				if (coupon_price > c.getPrice()){
					results.add(c);
				}
			}
			return results;
		}
		else {
			throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
		}
	}
	public Collection<Coupon> getCouponsExpiringBefore(long comp_id, Date end_date) throws ApplicationException {
		// Check whether company with this id exists
		if (companydao.getCompanyById(comp_id)!=null) {
			// Get all company coupons
			Collection<Coupon> allcoupons = companydao.getCoupons(comp_id);
			// Create temporary storage for results
			Collection<Coupon> results = new HashSet<Coupon>();
			// Iterate over all coupons and add those which have the end date earlier than required to results
			for (Coupon c : allcoupons){
/*				System.out.println("End date provided" + end_date);
				System.out.println("Coupon's end date" + c.getEndDate());*/
				if (end_date.after(c.getEndDate())){

					results.add(c);
				}
			}
			return results;
		}
		else {
			throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
		}
	}
}
