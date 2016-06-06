package webapi;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import beans.Coupon;
import exceptions.ApplicationException;
import facades.CompanyFacade;
import facades.CustomerFacade;

@Path("/")
public class CouponResource {
	
	@GET
	public Collection<Coupon> getCoupons(@Context HttpServletRequest request, @PathParam("customerId") long customerId, @PathParam("companyId") long companyId ) throws ApplicationException{
		
		HttpSession session = request.getSession(false);
		

		if (customerId!=0) {
			CustomerFacade customerFacade = (CustomerFacade) session.getAttribute("Facade");
			return customerFacade.getAllPurchasedCoupons(customerId);
		}
		if (companyId!=0) {
			CompanyFacade companyFacade = (CompanyFacade) session.getAttribute("Facade");
			return companyFacade.getAllCoupons(companyId);
		}
		return null;
	}
	
}
