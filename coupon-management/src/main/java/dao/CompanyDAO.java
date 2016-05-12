package dao;

import java.util.Collection;

import beans.Company;
import beans.Coupon;
import exceptions.ApplicationException;

public interface CompanyDAO {
	public long createCompany(Company company) throws ApplicationException;
	public void removeCompany(long comp_id) throws ApplicationException;
	public void updateCompany(Company company) throws ApplicationException;
	public Company getCompanyById(long comp_id) throws ApplicationException;
	public Company getCompanyByName(String comp_name) throws ApplicationException;
	public Collection<Company> getAllCompanies() throws ApplicationException;
	public Collection<Coupon> getCoupons(long comp_id) throws ApplicationException;
	public boolean login(String compName, String password) throws ApplicationException;
	boolean isCompanyNameExists(String compName) throws ApplicationException;
	
}
