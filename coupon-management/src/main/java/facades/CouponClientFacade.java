package facades;

import exceptions.ApplicationException;

public interface CouponClientFacade {
	public CouponClientFacade login(String login, String password, ClientType clientType) throws ApplicationException;
}
