package tasks;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimerTask;

import beans.Coupon;
import dao.CouponDAO;
import dbdao.CouponDBDAO;
import exceptions.ApplicationException;

public class DailyCouponExpirationTask extends TimerTask{
	
	private CouponDAO coupondao;
	
	@Override
	public void run() {
		try {
			DeleteExpiredCoupons();
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		
	}
	
	private void DeleteExpiredCoupons() throws ApplicationException {
		Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//Get all existing CouponResource from coupondao
		Collection<Coupon> allCoupons = coupondao.getAllCoupons();
		
		//Iterate over each coupon and check it's expiration date
		for (Coupon c : allCoupons){
			if (c.getEndDate().before(today)){
				//Remove the coupon
				coupondao.removeCoupon(c.getId());
			}
		}
	}

	public void StopTask() {
		this.cancel();
	}
	
	public DailyCouponExpirationTask(){
		
		coupondao = new CouponDBDAO();
	}
}
