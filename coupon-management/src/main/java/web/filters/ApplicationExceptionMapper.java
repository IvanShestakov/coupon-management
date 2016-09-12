package web.filters;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import exceptions.ApplicationException;
@Provider
@Produces(MediaType.APPLICATION_JSON) 
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException>{

	@Override
	public Response toResponse(ApplicationException ex) {
/*		ErrorMessage error = new ErrorMessage(ex.getMessage(), 500);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();*/
		String exceptionMessage = ex.errortype.toString();
		return Response.status(ex.errortype.getHttpErrorCode())
				.header("ExceptionMessage", exceptionMessage)
				.entity(ex.errortype).build();
	}

}
