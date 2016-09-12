package web.security;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import web.resources.CouponResource;
//This registration was required to enable custom mapping of security roles to access application resources
public class CouponAppResourceConfig extends ResourceConfig{
	 public CouponAppResourceConfig() {
         super(CouponResource.class);
         register(RolesAllowedDynamicFeature.class);
     }
}


