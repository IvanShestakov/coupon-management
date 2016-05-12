package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import beans.Coupon;
import beans.CouponType;
import beans.Customer;
import connection.ConnectionPool;
import dao.CustomerDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;

public class CustomerDBDAO implements CustomerDAO {

	public CustomerDBDAO() {
		super();
	}

	@Override
	public long createCustomer(Customer customer) throws ApplicationException {
		long id = -1; // Return -1 if the customer creation fails for some
						// reason.
		String sql = "INSERT INTO coupons.customer (cust_name, password) VALUES (?, ?);";
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, customer.getCustName());
			ps.setString(2, customer.getPassword());
			// System.out.println(ps.toString());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}

			ConnectionPool.returnConnection(connection);
		}

		return id;
	}

	@Override
	public void removeCustomer(long id) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		String sql = "DELETE FROM coupons.customer WHERE id= ? LIMIT 1;";

		try {
			ps = connection.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				ps.close();

			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}

	}

	@Override
	public void updateCustomer(Customer customer) throws ApplicationException {
		String sql = "UPDATE coupons.customer SET cust_name = ? , password = ? WHERE coupons.customer.id = ? LIMIT 1;";
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, customer.getCustName());
			ps.setString(2, customer.getPassword());
			ps.setLong(3, customer.getId());
			// System.out.println(ps.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}

			ConnectionPool.returnConnection(connection);
		}

	}

	@Override
	public Customer getCustomer(long id) throws ApplicationException {
		Customer customer = new Customer();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, cust_name, password FROM coupons.customer WHERE coupons.customer.id = ? LIMIT 1;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, id);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				customer.setId(rs.getLong("id"));
				customer.setCustName(rs.getString("cust_name"));
				customer.setPassword(rs.getString("password"));
			} else {
				throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);

		}

		return customer;
	}

	@Override
	public Customer getCustomerByName(String cust_name) throws ApplicationException {
		Customer customer = new Customer();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, cust_name, password FROM coupons.customer WHERE coupons.customer.cust_name = ? LIMIT 1;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, cust_name);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				customer.setId(rs.getLong("id"));
				customer.setCustName(rs.getString("cust_name"));
				customer.setPassword(rs.getString("password"));
			} else {
				throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);

		}

		return customer;
	}

	@Override
	public Collection<Customer> getAllCustomers() throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		Set<Customer> customers = new HashSet<Customer>();

		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT id, cust_name, password FROM coupons.customer;"); // Retrieve
																								// all
																								// customer
																								// records
																								// via
																								// select
																								// statement

			ResultSet resultset = statement.executeQuery();
			while (resultset != null && resultset.next()) { // Cycle through the
															// resulting rows,
															// and for each one
															// construct a
															// customer.

				Customer customer = new Customer();
				customer.setId(resultset.getLong("id"));
				customer.setCustName(resultset.getString("cust_name"));
				customer.setPassword(resultset.getString("password"));
				customers.add(customer); // Add the company to set of companies.
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {

			ConnectionPool.returnConnection(connection);
		}
		return customers;// Return the resulting set of customers;

	}

	@Override
	public Collection<Coupon> getCoupons(long customerId) throws ApplicationException {
		// Return all coupons for specific customer which id is provided
		Connection connection = ConnectionPool.getConnection();
		Set<Coupon> coupons = new HashSet<Coupon>();

		try {
			// First, a complex SQL statement is executed for all coupons of
			// specific customer.
			PreparedStatement statement = connection.prepareStatement(
					"SELECT coupon.id, coupon.title, coupon.start_date, coupon.end_date, coupon.amount, coupon.coupon_type, coupon.message, coupon.price, coupon.image  FROM customer_coupon, customer, coupon WHERE customer_coupon.cust_id = customer.id AND coupon.id = customer_coupon.coupon_id AND customer.id = ?;");
			statement.setLong(1, customerId);
			ResultSet resultset = statement.executeQuery();
			while (resultset != null && resultset.next()) {
				// Cycle through the resulting list of coupons for the
				// customer, and construct a coupon object and set it's
				// properties.
				Coupon coupon = new Coupon();
				coupon.setId(resultset.getLong("id"));
				coupon.setTitle(resultset.getString("title"));
				coupon.setStartDate(resultset.getDate("start_date"));
				coupon.setEndDate(resultset.getDate("end_date"));
				coupon.setAmount(resultset.getInt("amount"));
				coupon.setType(CouponType.valueOf(resultset.getString("coupon_type")));
				coupon.setMessage(resultset.getString("message"));
				coupon.setPrice(resultset.getDouble("price"));
				coupon.setImage(resultset.getString("image"));
				// Add the coupon to set of customers coupons.
				coupons.add(coupon); 
										
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {

			ConnectionPool.returnConnection(connection);
		}
		return coupons;

	}

	@Override
	public boolean login(String custName, String password) throws ApplicationException {
		// A special treatment for Admin user:
		if (custName == "Admin" && password == "1234") {
			return true;
		}
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, cust_name, password FROM coupons.customer WHERE coupons.customer.cust_name = ? AND coupons.customer.password = ? LIMIT 1;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, custName);
			ps.setString(2, password);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);

		}
	}

	@Override
	public Collection<Long> getCustomerIdByCouponId(long coupon_id) throws ApplicationException {
		// This method returns a list of customer ids who owns a specific coupon
		// (by its id)
		Set<Long> results = new HashSet<Long>();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT cust_id FROM coupons.customer_coupon WHERE coupons.customer_coupon.coupon_id = ?;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, coupon_id);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) {
				results.add(rs.getLong(1));
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return results;
	}

	@Override
	public boolean isCustomerNameExists(String cust_name) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id FROM coupons.customer WHERE coupons.customer.cust_name = ?;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, cust_name);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return false;
	}
	@Override
	public boolean isCustomerWithIdExists(Long customerId) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		Boolean exists = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//String sql = "SELECT id FROM coupons.customer WHERE coupons.customer.cust_name = ?;";
		String sql = "SELECT EXISTS(SELECT 1 FROM coupons.customer WHERE coupons.customer.id = ?);";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, customerId);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				exists = rs.getBoolean(1);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				rs.close();
				ps.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return exists;
		
	}
}
