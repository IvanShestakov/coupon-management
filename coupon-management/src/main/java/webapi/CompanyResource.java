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

import beans.Company;
import exceptions.ApplicationException;
import facades.AdminFacade;

@Path("/CompanyResource")

public class CompanyResource {
	private static final Logger log = LogManager.getLogger(CompanyResource.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Company> getCompanies(@Context HttpServletRequest request) throws ApplicationException {
		HttpSession session = request.getSession(false);
		log.debug("Session ID in CompanyResource" + session.getId());

		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		log.debug("Fetching ALL CompanyResource");
		return adminFacade.getAllCompanies();
	}
	
	
	@GET
	@Path("/{companyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Company getCompany(@Context HttpServletRequest request, @PathParam("companyId") long companyId) throws ApplicationException {
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		log.debug("Fetching company with id:" + companyId);
		return adminFacade.getCompany(companyId);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Company createCompany(@Context HttpServletRequest request, Company company) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		//TODO Refactor this method once create company returns a company instance
		Long newId = adminFacade.createCompany(company);
		log.debug("Created company " + company.getCompName());
		return adminFacade.getCompany(newId);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Company updateCompany(@Context HttpServletRequest request, Company company) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		//TODO Refactor this method once update company returns a company instance
		adminFacade.updateCompany(company);
		log.debug("Updated company " + company.getId());
		return adminFacade.getCompany(company.getId());
		
	}
	
	@DELETE
	//@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{companyId}")
	public void deleteCompany(@Context HttpServletRequest request,  @PathParam("companyId") long companyId) throws ApplicationException{
		HttpSession session = request.getSession(false);
		AdminFacade adminFacade = (AdminFacade) session.getAttribute("Facade");
		adminFacade.removeCompany(companyId);
		log.debug("Removed company " + companyId);
	}
	
	@Path("/{companyId}/CouponResource")
	public CouponResource getCompanyCoupons(){
		return new CouponResource();
	}
}