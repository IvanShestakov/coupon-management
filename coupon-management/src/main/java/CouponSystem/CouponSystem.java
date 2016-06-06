package CouponSystem;

import java.sql.SQLException;
import java.util.Timer ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import connection.ConnectionPool;
import exceptions.ApplicationException;
import facades.AdminFacade;
import facades.ClientType;
import facades.CompanyFacade;
import facades.CouponClientFacade;
import facades.CustomerFacade;
import tasks.DailyCouponExpirationTask;
import webapi.CustomerResource;


public class CouponSystem {
	private static final Logger log = LogManager.getLogger(CustomerResource.class);
	
	private static CouponSystem INSTANCE = new CouponSystem();
	private tasks.DailyCouponExpirationTask dailyCouponExpiration;
	private int delay;
	private int period;
	
	private CouponSystem(){
		dailyCouponExpiration = new DailyCouponExpirationTask();
		delay = 0;
		period = 1000*60*60*24;
		Timer timer = new Timer("DailyExpirationTimer");
		timer.scheduleAtFixedRate(dailyCouponExpiration, delay, period);
	}
	
	public static CouponSystem getInstance(){
		log.debug("Coupon System get instance() was called");
		return INSTANCE;
	}
	
	public void shutDown(){
		//Stop the coupon expiration task
		dailyCouponExpiration.StopTask();
		//Close all connections of the connection pool
		try {
			ConnectionPool.closeAllConnections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Shutdown the runtime
		System.exit(0);
	}
	public CouponClientFacade login(String login, String password, ClientType type) throws ApplicationException{
		//TODO Review this solution with other guys
		log.debug("Entered CouponClientFacade login() method");
		log.debug("User" + login + " Password " + password + "Type " + type);
		CouponClientFacade result = null;
		switch (type) {
		case ADMIN:
			log.debug("DEBUG: Entered case ADMIN");
			result = new AdminFacade();
			return result.login(login, password, type);
			
		case COMPANY:
			log.debug("DEBUG: Entered case COMPANY");
			result = new CompanyFacade();
			return result.login(login, password, type);
			
		case CUSTOMER:
			log.debug("DEBUG: Entered case CUSTOMER");
			result = new CustomerFacade();
			return result.login(login, password, type);
		default:
			break;
		}
		return result;
		
		
	}
}
