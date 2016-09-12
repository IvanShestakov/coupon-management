package web.resources;

import java.sql.Date;
import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
@PermitAll
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
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/customer/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCouponsForCustomer(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getAllPurchasedCoupons(customerId);
	}
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/customer/{customerId}/byType/{couponType}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomerByType(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId, @PathParam ("couponType") CouponType type) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getPurchasedCouponsByType(customerId, type);
		
	}
	@RolesAllowed("CUSTOMER")
	@GET
	@Path("/customer/{customerId}/byPrice/{couponPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getCouponsForCustomerByPrice(@Context HttpServletRequest request,
			@PathParam("customerId") long customerId, @PathParam ("couponPrice") long price) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
		return customerFacade.getPurchasedCouponsByPrice(customerId, price);
		
	}
	@RolesAllowed("CUSTOMER")
	@POST
	@Path("/customer/{customerId}/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon purchaseCoupon(@Context HttpServletRequest request, @PathParam("customerId") long customerId, @PathParam ("couponId") long couponId ) throws ApplicationException{
	HttpSession session = request.getSession(false);
	CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
	return customerFacade.purchaseCouponForCustomer(couponId, customerId);
	}
	@RolesAllowed("COMPANY")
	@GET
	@Path("/company/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Coupon> getAllCouponsForCompany(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId) throws ApplicationException {

		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getAllCoupons(companyId);

	}
	@RolesAllowed("COMPANY")
	@GET
	@Path("/company/{companyId}/{couponId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon getACouponForCompany(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @PathParam ("couponId") long couponId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCoupon(couponId);
	}
	@RolesAllowed("COMPANY")
	@GET
	@Path("/company/{companyId}/byType/{couponType}")
	@Produces(MediaType.APPLICATION_JSON)
	public  Collection<Coupon> getCompanyCouponsByType (@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @PathParam ("couponType") CouponType type) throws ApplicationException{

		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponByType(companyId, type);
	}
	@RolesAllowed("COMPANY")
	@GET
	@Path("/company/{companyId}/byPrice/{couponPrice}")
	@Produces(MediaType.APPLICATION_JSON)
	public  Collection<Coupon> getCompanyCouponsByPrice(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @PathParam ("couponPrice") double price) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponsCheaperThan(companyId, price);
	}
	
	@GET
	@Path("/company/{companyId}/byDate/{endDateBefore}")
	@Produces(MediaType.APPLICATION_JSON)
	public  Collection<Coupon> getCompanyCouponsExpiringBefore(@Context HttpServletRequest request,
			@PathParam("companyId") long companyId, @PathParam ("endDateBefore") Date expDate) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.getCouponsExpiringBefore(companyId, expDate );
	}
	
	@POST
	@Path("/company/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon createCouponForCompany(@Context HttpServletRequest request,
			Coupon coupon, @PathParam("companyId") long companyId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.CreateCoupon(coupon, companyId);
	}
	
	@PUT
	@Path("/company/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Coupon updateCouponForCompany(@Context HttpServletRequest request,
	Coupon coupon, @PathParam("companyId") long companyId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		return companyFacade.UpdateCoupon(coupon);
	}
	
	@DELETE
	@Path("/company/{companyId}/{couponId}")
	public void removeCouponForCompany(@Context HttpServletRequest request, @PathParam("companyId") long companyId, @PathParam ("couponId") long couponId) throws ApplicationException {
		log.debug("Removing coupon id:" + couponId + "for company id:" + companyId);
		HttpSession session = request.getSession(false);
		CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
		companyFacade.RemoveCoupon(couponId);
	}
	
}
