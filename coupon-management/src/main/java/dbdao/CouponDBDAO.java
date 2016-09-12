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
import connection.ConnectionPool;
import dao.CouponDAO;
import exceptions.ApplicationException;
import exceptions.ErrorType;

public class CouponDBDAO implements CouponDAO {
	public CouponDBDAO() {
		super();
	}

	@Override
	public long createCoupon(Coupon coupon, Long companyId) throws ApplicationException {
		// Creates the representation of a coupon in the database, returns
		// coupon id as result of successful creation. -1 returns if failed to
		// create.
		long id = -1;
		String sql = "INSERT INTO coupons.coupon (title, start_date, end_date, amount, coupon_type, message, price, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		String sql_join = "INSERT INTO coupons.company_coupon (comp_id, coupon_id) VALUES (?, ?);";
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null, ps_join = null;
		ResultSet rs = null;
		try {
			// Update the coupon table
			ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, coupon.getTitle());
			ps.setDate(2, coupon.getStartDate());
			ps.setDate(3, coupon.getEndDate());
			ps.setInt(4, coupon.getAmount());
			ps.setString(5, coupon.getType().name());
			ps.setString(6, coupon.getMessage());
			ps.setDouble(7, coupon.getPrice());
			ps.setString(8, coupon.getImage());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				id = rs.getLong(1);
			}
			// Update the company_coupon table
			ps_join = connection.prepareStatement(sql_join);
			ps_join.setLong(1, companyId);
			ps_join.setLong(2, id);
			ps_join.executeUpdate();

		} catch (SQLException e) {
			throw new ApplicationException(ErrorType.FAILED_TO_EXECUTE_QUERY, e);
		} finally {
			try {
				rs.close();
				ps.close();
				ps_join.close();
			} catch (SQLException e) {
				throw new ApplicationException(ErrorType.FAILED_TO_CLOSE_DB_CONNECTION, e);
			}

			ConnectionPool.returnConnection(connection);
		}

		return id;
	}

	@Override
	public void removeCoupon(long id) throws ApplicationException {
		// The SQL statement here deletes the coupon both from coupons table and
		// from company_coupon table
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		String sql = "DELETE coupons.coupon, coupons.company_coupon, coupons.customer_coupon "
				+ "FROM coupons.coupon "
				+ "JOIN coupons.company_coupon "
				+ "LEFT JOIN coupons.customer_coupon ON coupons.coupon.id=coupons.customer_coupon.coupon_id "
				+ "WHERE coupons.coupon.id=coupons.company_coupon.coupon_id AND coupons.coupon.id = ?;";

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
	public Coupon updateCoupon(Coupon coupon) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;

		String sql = "UPDATE coupons.coupon SET title = ?, start_date = ?, end_date = ?, amount = ?, coupon_type = ?, message = ?, price = ?, image = ? WHERE coupons.coupon.id = ? LIMIT 1;";

		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, coupon.getTitle());
			ps.setDate(2, coupon.getStartDate());
			ps.setDate(3, coupon.getEndDate());
			ps.setInt(4, coupon.getAmount());
			ps.setString(5, coupon.getType().name());
			ps.setString(6, coupon.getMessage());
			ps.setDouble(7, coupon.getPrice());
			ps.setString(8, coupon.getImage());
			ps.setLong(9, coupon.getId());
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
		return coupon;
	}

	@Override
	public Coupon getCoupon(long id) throws ApplicationException {
		Coupon coupon = new Coupon();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, title, start_date, end_date, amount, coupon_type, message, price, image FROM coupons.coupon WHERE coupon.id = ? LIMIT 1;";
		try {
			ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setLong(1, id);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				coupon.setId(rs.getLong("id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("start_date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				coupon.setType(CouponType.valueOf(rs.getString("coupon_type")));
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
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

		return coupon;
	}

	@Override
	public Collection<Coupon> getAllCoupons() throws ApplicationException {
		Set<Coupon> allcoupons = new HashSet<Coupon>();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, title, start_date, end_date, amount, coupon_type, message, price, image FROM coupons.coupon;"; // Query
		try {
			ps = connection.prepareStatement(sql);
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) { // iterate over query results and
												// add each result into the
												// hashset.
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong("id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("start_date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				coupon.setType(CouponType.valueOf(rs.getString("coupon_type")));
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
				allcoupons.add(coupon);
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

		return allcoupons; // return the resulting set of coupon objects

	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType coupontype) throws ApplicationException {
		Set<Coupon> couponsoftype = new HashSet<Coupon>();
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT id, title, start_date, end_date, amount, coupon_type, message, price, image FROM coupons.coupon WHERE coupon_type = ?;"; // Query
																																						// all
																																						// coupons
																																						// with
																																						// specified
																																						// type
																																						// from
																																						// coupon
																																						// table.
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, coupontype.name());
			// System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs != null && rs.next()) { // iterate over query results and
												// add each result into the set.
				Coupon coupon = new Coupon();
				coupon.setId(rs.getLong("id"));
				coupon.setTitle(rs.getString("title"));
				coupon.setStartDate(rs.getDate("start_date"));
				coupon.setEndDate(rs.getDate("end_date"));
				coupon.setAmount(rs.getInt("amount"));
				coupon.setType(CouponType.valueOf(rs.getString("coupon_type")));
				coupon.setMessage(rs.getString("message"));
				coupon.setPrice(rs.getDouble("price"));
				coupon.setImage(rs.getString("image"));
				couponsoftype.add(coupon);
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

		return couponsoftype; // return the resulting set of coupon objects
	}

	@Override
	public void purchaseCouponForCustomer(long couponId, long custId) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		String sql = "INSERT INTO coupons.customer_coupon (cust_id, coupon_id) VALUES (?, ?);";

		try {
			ps = connection.prepareStatement(sql);
			ps.setLong(1, custId);
			ps.setLong(2, couponId);
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
	public void removeCouponForCustomer(long couponId, long custId) throws ApplicationException {
		Connection connection = ConnectionPool.getConnection();
		PreparedStatement ps = null;
		String sql = "DELETE FROM coupons.customer_coupon WHERE coupon_id= ? AND cust_id=?;";

		try {
			ps = connection.prepareStatement(sql);
			ps.setLong(1, couponId);
			ps.setLong(2, custId);
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

}
