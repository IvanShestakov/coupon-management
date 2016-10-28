package facades;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beans.Company;
import beans.Coupon;
import beans.Customer;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;



public class AdminFacade implements CouponClientFacade {
	private static final Logger log = LogManager.getLogger(CouponClientFacade.class);

	
	private CompanyDAO companydao;
	private CustomerDAO customerdao;
	private CouponDAO coupondao;


	private Long id = 1L;

	@Override
	public CouponClientFacade login(String login, String password, ClientType clientType) throws ApplicationException {
		log.debug("Entered AdminFacade login method");
		if (login.equals("Admin") && password.equals("1234") && clientType.equals(ClientType.ADMIN)) {
			log.debug("Successfully authenticated on AdminFacade login method");
			return new AdminFacade();
		}
		log.debug("Failed to authenticate on AdminFacade login method");
		throw new ApplicationException(ErrorType.FAILED_TO_LOGIN);
	}

	public AdminFacade() {
		companydao = new CompanyDBDAO();
		customerdao = new CustomerDBDAO();
		coupondao = new CouponDBDAO();
	}
	
	public long createCompany(Company company) throws ApplicationException {
		// Check if company with same name already exists. If not, call dao
		// function to create company. Return new company id back to calling
		// method.
		if (!companydao.isCompanyNameExists(company.getCompName())) {
			company.setId(companydao.createCompany(company));
			return company.getId();
		} else {
			throw new ApplicationException(ErrorType.COMPANY_ALREADY_EXISTS);
		}
	}
	//TODO refactor the method to return Company instance.
	public void removeCompany(long id) throws ApplicationException {

		// To remove the company, validate it exists: if it
		// doesn't - consider removal complete.
		Company company = companydao.getCompanyById(id);
		if (company == null) {
			return;
		}
		// Get the list of all company's coupons.
		Collection<Coupon> coupons = companydao.getCoupons(id);
		// Iterate over list of coupons.
		for (Coupon c : coupons) {
			long couponId = c.getId();
			// Delete the coupon from coupons table as well as from join tables.
			coupondao.removeCoupon(couponId);
		}
		// Finally delete the company.
		companydao.removeCompany(id);
	}
	
	//TODO refactor the method to return Company instance.
	public void updateCompany(Company company) throws ApplicationException {
		//Retrieve from database a company with provided id.
		Company companyindb = companydao.getCompanyById(company.getId());
		//If stored company name equals the provided name, update the company, otherwise throw exception
		if (companyindb.getCompName().equals(company.getCompName())) {
			companydao.updateCompany(company);
		}
		else {
			throw new ApplicationException(ErrorType.ENTITY_RENAME_NOT_ALLOWED);
		}
	}
	public Company getCompany(Long id) throws ApplicationException{
		Company company = companydao.getCompanyById(id);
		return company;
	}

	public Collection<Company> getAllCompanies() throws ApplicationException {
		// Fetch all the CompanyResource from the persistence layer
		Collection<Company> allcompanies = companydao.getAllCompanies();
		return allcompanies;
	}
	
	
	public long createCustomer(Customer customer) throws ApplicationException {
		
		// if customer with this name doesn't exist - create it and return it's id.
		if (!customerdao.isCustomerNameExists(customer.getCustName())){
			customer.setId(customerdao.createCustomer(customer));
			return customer.getId();
		}
		else {
			throw new ApplicationException(ErrorType.CUSTOMER_ALREADY_EXISTS);
			
		}
		
	
	}
	//TODO refactor the method to return Customer instance.
	public void removeCustomer(long cust_id) throws ApplicationException {

		// Before removal of the customer, iterate over their purchased coupons and remove them.
		Collection<Coupon> coupons = customerdao.getCoupons(cust_id);
		for (Coupon c : coupons) {
			coupondao.removeCouponForCustomer(c.getId(), cust_id);
		}
		// When all coupons have been removed, delete the customer
		customerdao.removeCustomer(cust_id);
		
		
	}
	//TODO refactor the method to return Customer instance.
	public void updateCustomer(Customer customer) throws ApplicationException {
		//Retrieve from database a customer with provided id.
				Customer customerindb = customerdao.getCustomer(customer.getId());
				//If stored customer name equals the provided name, update the customer, otherwise throw exception
				if (customerindb.getCustName().equals(customer.getCustName())) {
					customerdao.updateCustomer(customer);
				}
				else {
					throw new ApplicationException(ErrorType.ENTITY_RENAME_NOT_ALLOWED);
				}
			}
	

	public Customer getCustomer(long id) throws ApplicationException {
		Customer customer = customerdao.getCustomer(id);
		return customer;
		
	}

	public Collection<Customer> getAllCustomers() throws ApplicationException {
		Collection<Customer> allcustomers = customerdao.getAllCustomers();
		return allcustomers;
	}

	@Override
	public Long getId() {
		return this.id ;
	}

}
