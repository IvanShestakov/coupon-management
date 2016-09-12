package web.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class CORSFilter implements ContainerResponseFilter {
 //Added this filter to support server and client side to run in separate web containers - for ease of development
	private static final Logger log = LogManager.getLogger(CORSFilter.class);
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
/*		log.debug("Entered CORS Filter");
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Accept, x-auth-token, "
	                + "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		*/
	}


 
}