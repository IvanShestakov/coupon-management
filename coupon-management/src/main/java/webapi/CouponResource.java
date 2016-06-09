package webapi;

import java.sql.Date;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.Coupon;
import beans.CouponType;
import exceptions.ApplicationException;
import facades.CompanyFacade;
import facades.CouponFacade;
import facades.CustomerFacade;

@Path("/coupons")
public class CouponResource {
	private static final Logger log = LogManager.getLogger(CompanyResource.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCoupons(@Context HttpServletRequest request) throws ApplicationException {
		// Return all coupons, regardless of login status
		log.debug("Returning all coupons");

		CouponFacade couponFacade = new CouponFacade();
		return couponFacade.getAllCoupons();

	}

	@GET
	@Path("/customer")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCouponsForCustomer(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getAllPurchasedCoupons(customerId);
	}
	
	@GET
	@Path("coupons/customer/{customerId}")
	public Collection<Coupon> getCouponsForCustomerByType(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId, @QueryParam ("byType") CouponType type) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getPurchasedCouponsByType(customerId, type);
		
	}
	
	@GET
	@Path("coupons/customer/{customerId}")
	public Collection<Coupon> getCouponsForCustomerByPrice(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId, @QueryParam ("priceUnder") long price) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getPurchasedCouponsByPrice(customerId, price);
		
	}
	
	@POST
	@Path("coupons/customer/{customerId}/{couponId}")
	
	public Coupon purchaseCoupon(@Context HttpServletRequest request, @PathParam("customerId") long customerId, @PathParam ("couponId") long couponId ) throws ApplicationException{
	HttpSession session = request.getSession(false);
	CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
	return customerFacade.purchaseCouponForCustomer(couponId, customerId);
	}
	
	@GET
	@Path("/company")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCouponsForCompany(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId) throws ApplicationException {

		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getAllCoupons(companyId);

	}

	@GET
	@Path("coupons/company/{companyId}/{couponId}")
	public Coupon getACouponForCompany(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @PathParam ("couponId") long couponId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCoupon(couponId);
	}

	@GET
	@Path("coupons/company/{companyId}")
	public  Collection<Coupon> getCompanyCouponsByType (@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @QueryParam ("byType") CouponType type) throws ApplicationException{

		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponByType(companyId, type);
	}
	
	@GET
	@Path("coupons/company/{companyId}")
	public  Collection<Coupon> getCompanyCouponsByPrice(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @QueryParam ("priceUnder") double price) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponsCheaperThan(companyId, price);
	}
	
	@GET
	@Path("coupons/company/{companyId}")
	public  Collection<Coupon> getCompanyCouponsExpiringBefore(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @QueryParam ("endDateBefore") Date expDate) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponsExpiringBefore(companyId, expDate );
	}
	
	@POST
	@Path("coupons/company/{companyId}")
	public Coupon createCouponForCompany(@Context HttpServletRequest request,
			Coupon coupon, @PathParam("companyId") long companyId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.CreateCoupon(coupon, companyId);
	}
	
	@PUT
	@Path("coupons/company/{companyId}")
	public Coupon updateCouponForCompany(@Context HttpServletRequest request,
	Coupon coupon, @PathParam("companyId") long companyId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.UpdateCoupon(coupon);
	}
	
	@DELETE
	@Path("coupons/company/{companyId}/{couponId}")
	public void removeCouponForCompany(@Context HttpServletRequest request, @PathParam("companyId") long companyId, @PathParam ("couponId") long couponId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		companyFacade.RemoveCoupon(couponId);
	}
	
}
