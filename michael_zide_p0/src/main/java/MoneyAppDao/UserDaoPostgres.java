package MoneyAppDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ConnectionUtil.ConnectionUtil;
import MoneyAppPojos.Customer;

public class UserDaoPostgres implements UserDao {
	
		private PreparedStatement statement;
		private static Logger log = Logger.getLogger("Web");
		public ConnectionUtil connUtil = new ConnectionUtil();
		
		public void setConnUtil(ConnectionUtil connUtil) {
			this.connUtil = connUtil;
		}
		
		@Override
		public boolean createCustomer(Customer user) throws SQLException {
			
			String sql = "insert into customer (username,passphrase,email,phone_num,first_name,last_name) "
					+ "values(?,?,?,?,?,?);";

			try {
				statement = connUtil.createConnection().prepareStatement(sql);
				statement.setString(1,user.getUsername());
				statement.setString(2,user.getPassword());
				statement.setString(3,user.getEmail());
				statement.setString(4,user.getPhoneNum());
				statement.setString(5,user.getFirstName());
				statement.setString(6,user.getLastName());
				statement.executeUpdate();
				
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("SQLException:" + e);
				return false;		
			}
		}
	
		@Override
		public Customer readCustomer(String username) throws SQLException {
			
			
			String sql = "select * from customer where username = ?;";

			try(Connection conn = connUtil.createConnection()) {
					
				statement = conn.prepareStatement(sql);
				statement.setString(1, username);
				ResultSet rs = statement.executeQuery();
				rs.next();
				
				Customer returnedCustomer = new Customer(rs.getString(1),
														 rs.getString(2),
														 rs.getString(3),
														 rs.getString(4),
														 rs.getString(5),
														 rs.getString(6));
				
				return returnedCustomer;
					
			}catch (SQLException e) {
				log.error("SQLException:" + e);
				return null;
			}
		}
	
		@Override
		public List<Customer> readAllCustomers() {
			
			List<Customer> allCust = new ArrayList<>();
			
			String sql = "select * from customer";
			
			try {
					statement = connUtil.createConnection().prepareStatement(sql);
					ResultSet rs = statement.executeQuery();
					while(rs.next()) {
						Customer returnedCustomer = new Customer(rs.getString(1),
						rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6));
						
						allCust.add(returnedCustomer);
					
					}
					return allCust;
					
			}catch(SQLException e) {
				log.error("SQLException:" + e);
				return null;
			}
		}
	
		@Override
		public boolean updateCustomer(Customer user) {
			
			try { 
	            String sql = "UPDATE customer SET passphrase = ?, " 
	        			+ "email = ?, phone_num = ?, first_name = ?, last_name = ? "
	        			+ "WHERE username = ?;";

	            statement = connUtil.createConnection().prepareStatement(sql);
	            statement.setString(1, user.getPassword());
	            statement.setString(2, user.getEmail());
	            statement.setString(3, user.getPhoneNum());
	            statement.setString(4, user.getFirstName());
	            statement.setString(5, user.getLastName());
	            statement.setString(6, user.getUsername());
	            
	            statement.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	        	log.error("SQLException:" + e);
	        	return false;
			}
		}
	
		@Override
		public boolean deleteCustomer(String username) {
			
			//Currently not implemented
			String sql = "delete from customer where username = ?;";
			
			try {
				statement = connUtil.createConnection().prepareStatement(sql);
				statement.setString(1, username);
				statement.executeUpdate();
				return true;
			}catch (SQLException e) {
				log.error("SQLException:" + e);
				return false;
			}
		}

}
