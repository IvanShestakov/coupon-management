package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import beans.Company;
import beans.Coupon;
import beans.CouponType;
import connection.ConnectionPool;
import dao.CompanyDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;

public class CompanyDBDAO implements CompanyDAO {

	private Connection connection;

	public CompanyDBDAO() {
		super();

	}

	@Override
	public long createCompany(Company company) throws ApplicationException {
		// Creates the representation of a company in the database, returns
		// company id as result of successful creation. -1 returns if failed to
		// create.
		long id = -1;
		connection = ConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(
					"INSERT INTO company (comp_name, password, email) VALUES ( ?, ? ,?);",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, company.getCompName());
			statement.setString(2, company.getPassword());
			statement.setString(3, company.getEmail());

			statement.executeUpdate();

			rs = statement.getGeneratedKeys();
			if (rs != null && rs.next()) {
				id = rs.getLong(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);

		} finally {
			try {
				rs.close();
				statement.close();
				ConnectionPool.returnConnection(connection);
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
		}
		return id;
	}

	@Override
	public boolean isCompanyNameExists(String compName) throws ApplicationException {
		boolean exists = true;// Expect the worse - that company exists already.
		connection = ConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		// Check if company with this name already exists.
		try {
			statement = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM company WHERE comp_name = ?);");
			statement.setString(1, compName);

			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				exists = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				statement.close();
				rs.close();
				ConnectionPool.returnConnection(connection);
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
		}
		return exists;
	}

	@Override
	public void updateCompany(Company company) throws ApplicationException {
		// Updates the selected company with new details.
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement statement = null;
		try {
			statement = connection
					.prepareStatement("UPDATE company SET comp_name=?, email=?, password=? WHERE id=? LIMIT 1;");
			statement.setString(1, company.getCompName());
			statement.setString(2, company.getEmail());
			statement.setString(3, company.getPassword());
			statement.setLong(4, company.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}

	}

	@Override
	public Company getCompanyById(long id) throws ApplicationException {
		// Try to retrieve the company with provided id from database.
		Company company = new Company();
		PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection connection = ConnectionPool.getConnection();
		try {
			statement = connection.prepareStatement("SELECT id, comp_name, password, email FROM company WHERE id = ?;");
			statement.setLong(1, id);
			resultset = statement.executeQuery();

			if (resultset != null && resultset.next()) {
				company.setId(resultset.getLong("id"));
				company.setCompName(resultset.getString("comp_name"));
				company.setPassword(resultset.getString("password"));
				company.setEmail(resultset.getString("email"));
			} else {
				throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				resultset.close();
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return company;

	}

	@Override
	public Company getCompanyByName(String comp_name) throws ApplicationException {
		// Try to retrieve the company with provided company name from database.
		Company company = new Company();
		PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection connection = ConnectionPool.getConnection();
		try {
			statement = connection
					.prepareStatement("SELECT id, comp_name, password, email FROM company WHERE comp_name = ?;");
			statement.setString(1, comp_name);
			resultset = statement.executeQuery();

			if (resultset != null && resultset.next()) {
				company.setId(resultset.getLong("id"));
				company.setCompName(resultset.getString("comp_name"));
				company.setPassword(resultset.getString("password"));
				company.setEmail(resultset.getString("email"));
			} else {
				throw new ApplicationException(ErrorType.ENTITY_DOES_NOT_EXIST_IN_DB);
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				resultset.close();
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return company;

	}

	@Override
	public Collection<Company> getAllCompanies() throws ApplicationException {
		// Retrieve all company records from database.
		Connection connection = ConnectionPool.getConnection();
		Set<Company> companies = new HashSet<Company>();
		PreparedStatement statement = null;
		ResultSet resultset = null;
		String sql = "SELECT id, comp_name, password, email FROM company;";
		try {
			// Retrieve all company records via select statement
			statement = connection
					.prepareStatement(sql);
			resultset = statement.executeQuery();
			while (resultset != null && resultset.next()) {
				// Cycle through the resulting rows, and for each one construct
				// a company.
				Company company = new Company();
				company.setId(resultset.getLong("id"));
				company.setCompName(resultset.getString("comp_name"));
				company.setPassword(resultset.getString("password"));
				company.setEmail(resultset.getString("email"));

				companies.add(company); // Add the company to set of CompanyResource.
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}
		finally {
			try {
				resultset.close();
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			
			
			ConnectionPool.returnConnection(connection);
		}
		return companies;

	}

	@Override
	public boolean login(String compName, String password) throws ApplicationException {
		// Method which returns true if company name matches the provided
		// password.
		boolean login = false;// Start with assumption that it's not.
		connection = ConnectionPool.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "SELECT EXISTS(SELECT 1 FROM company WHERE comp_name = ? and password = ?);";
		try {
			statement = connection
					.prepareStatement(sql);
			statement.setString(1, compName);
			statement.setString(2, password);

			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				login = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				rs.close();
				statement.close();
			}
			catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}
		return login;
	}

	@Override
	public void removeCompany(long id) throws ApplicationException {
		// Remove the company with provided id from the database
		connection = ConnectionPool.getConnection();
		PreparedStatement statement = null;
		String sql = "DELETE FROM company WHERE id = ? LIMIT 1;";
		try {
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			ConnectionPool.returnConnection(connection);
		}

	}

	@Override
	public Collection<Coupon> getCoupons(long id) throws ApplicationException {
		// Return all CouponResource for specific company which id is provided
		Connection connection = ConnectionPool.getConnection();
		Set<Coupon> coupons = new HashSet<Coupon>();
		PreparedStatement statement = null;
		ResultSet resultset = null;
		try {
			// First, a complex SQL statement is executed for all CouponResource of
			// specific company.
			statement = connection.prepareStatement(
					"SELECT coupon.id, coupon.title, coupon.start_date, coupon.end_date, coupon.amount, coupon.coupon_type, coupon.message, coupon.price, coupon.image  FROM company_coupon, company, coupon WHERE company_coupon.comp_id = company.id AND coupon.id = company_coupon.coupon_id AND company.id = ?;");
			statement.setLong(1, id);
			resultset = statement.executeQuery();
			while (resultset != null && resultset.next()) { 
			// Cycle through the resulting list of CouponResource for the
			// company, and construct a coupon object and
			// set it's properties.
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
				// Add the coupon to set of company CouponResource.
				coupons.add(coupon); 
			}

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		}

		finally {
			try {
				resultset.close();
				statement.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}
			
			ConnectionPool.returnConnection(connection);
		}
		return coupons;

	}

}
