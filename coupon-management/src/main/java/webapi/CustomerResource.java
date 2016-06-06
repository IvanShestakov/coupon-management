package webapi;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
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

import com.sun.media.jfxmedia.Media;

import beans.Company;
import beans.Customer;
import exceptions.ApplicationException;
import facades.AdminFacade;

@Path("/CustomerResource")

public class CustomerResource {
	private static final Logger log = LogManager.getLogger(CustomerResource.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Customer> getCustomers(@Context HttpServletRequest request) throws ApplicationException {
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		log.debug("Fetching ALL CustomerResource" );
		return adminFacade.getAllCustomers();
	}
	
	
	@GET
	@Path("/{customerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer(@Context HttpServletRequest request, @PathParam("customerId") long customerId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		log.debug("Returning customer with id: " + customerId);
		return adminFacade.getCustomer(customerId);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Customer createCustomer(@Context HttpServletRequest request, Customer customer) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		Long newId = adminFacade.createCustomer(customer);
		log.debug("Created customer " + newId);
		return adminFacade.getCustomer(newId);
	}
	
	@DELETE
	//@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{customerId}")
	public void deleteCustomer(@Context HttpServletRequest request,  @PathParam("customerId") long customerId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		adminFacade.removeCustomer(customerId);
		log.debug("Removed customer " + customerId);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{customerId}")
	public Customer updateCustomer(@Context HttpServletRequest request, Customer customer) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		adminFacade.updateCustomer(customer);
		log.debug("Updated customer " + customer.getCustName());
		return customer;
	}
	
	@Path("/{customerId}/CouponResource")
	public CouponResource getCustomerCoupons(){
		return new CouponResource();
	}
	
}

	