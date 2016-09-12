package web.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.User;
import facades.CouponClientFacade;
import web.security.CouponsManagementSecurityContext;
@Provider
@PreMatching
public class LoginRequestFilter implements ContainerRequestFilter {
	private static final Logger log = LogManager.getLogger(LoginRequestFilter.class);

	@Context
    private HttpServletRequest httpRequest;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// This filter verifies that user is logged in, unless the request to
		// login or logout is made.
		
		if (requestContext.getUriInfo().getPath().contains("login") ||
				requestContext.getUriInfo().getPath().contains("logout")) {
			return;
			}
		log.debug("Entered Login Request Filter");
		// Try getting login attributes from session.
		HttpSession session = httpRequest.getSession(false);
		if (session!=null){
		User user = (User) session.getAttribute("User");
		CouponClientFacade facade = (CouponClientFacade) session.getAttribute("Facade");
		Class<?>[] interfaces = facade.getClass().getInterfaces();

		if (interfaces.length > 0) {
			for (Class<?> iface : interfaces) {
				if (iface.equals(CouponClientFacade.class)) {
					requestContext.setSecurityContext(new CouponsManagementSecurityContext(user , facade ));
					log.debug("Login Request Filter validated that user is authorized.");
					return;
				}
			}

		}
		}
		// If the user is unauthorized - return appropriate response
		log.debug("Unauthorized access registered.");
		Response unauthorized = Response.status(Response.Status.UNAUTHORIZED)
				.entity("Unauthorized access to this resource.").build();
		requestContext.abortWith(unauthorized);
		
		
	}



}