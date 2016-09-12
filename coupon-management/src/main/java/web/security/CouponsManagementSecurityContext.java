package web.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import beans.User;
import facades.CouponClientFacade;
/*
*Custom Security Context for Coupon Management Web
*/
public class CouponsManagementSecurityContext implements SecurityContext{
	private User user;
	private CouponClientFacade facade;
	//private String scheme;
	
	public CouponsManagementSecurityContext(User user, CouponClientFacade facade){
		this.user = user;
		this.facade = facade;
		//this.scheme = scheme;
	}
	
	@Override
	public String getAuthenticationScheme() {
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return (Principal) this.user;
	}
	
	public CouponClientFacade getFacade(){
		return this.facade;
	}
	@Override
	public boolean isSecure() {
		//return "https".equals(this.scheme);
		return false;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (user.getType() != null) {
			return user.getType().name().equals(role);
		}
		return false;
	}

}
