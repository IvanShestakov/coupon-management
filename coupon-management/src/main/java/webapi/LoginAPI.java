package webapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 * Use the following concept to create json objects
 * @GET
 * public JsonArray developers(@Context HTTPHeaders http)
 * 		JsonObject object = Json.createObjectBuilder().add("firstname", "john").build();
 *		return Json.createArrayBuilder().add(object).build();
 * 
 * Implement ExceptionMapper<ExceptionType> to send appropriate response to client when an exception occurs.
 * @Provider
 * public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> (){
 * @Override
 * public Response toResponse(RuntimeException exception){
 * 	return Response.status(Status.INTERNAL_SERVER_ERROR).header("x-reason", exception.getMessage()).build();
 * }
 * }
 */

import CouponSystem.CouponSystem;
import beans.User;
import exceptions.ApplicationException;
import facades.CouponClientFacade;

@Path("/login")
public class LoginAPI {
	private static final Logger log = LogManager.getLogger(LoginAPI.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)

	public void login(@Context HttpServletRequest request, @Context HttpServletResponse response, User user)
			throws ApplicationException {
		log.debug( "Entered login method for LoginAPI");
		log.debug( "Client Type is of class: " + user.getType().getClass());
		CouponClientFacade facade = CouponSystem.getInstance().login(user.getUsername(), user.getPassword(),
				user.getType());
		if (facade != null) {
			log.info("Successfully authenticated user " + user.getUsername());
			HttpSession session = request.getSession(true);
			session.setAttribute("Facade", facade);
			session.setMaxInactiveInterval(10 * 60);
			
			log.debug("Session ID in login" + session.getId());
		} else {
			log.debug("Failed to authenticate user: " + user.getUsername() + " Password:"
					+ user.getPassword() + " User Type: " + user.getType());

		}
		// return Response.status(Status.UNAUTHORIZED).build();

	}

}
