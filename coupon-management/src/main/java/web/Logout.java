package web;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exceptions.ApplicationException;

@Path("/logout")
public class Logout {
	private static final Logger log = LogManager.getLogger(Logout.class);
	@POST
	public void logout(@Context HttpServletRequest request)
			throws ApplicationException {
				log.debug("Logout requested");
				if (request.getSession(false)!=null){
					request.getSession(false).invalidate();
				}
			}
}