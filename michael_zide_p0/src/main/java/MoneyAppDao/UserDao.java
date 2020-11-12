package MoneyAppDao;

import java.sql.SQLException;
import java.util.List;

import MoneyAppPojos.Customer;

public interface UserDao {
		/**
		 * Create user in db
		 */
		public boolean createCustomer (Customer user) throws SQLException;
		
		public Customer readCustomer(String username) throws SQLException;
		
		public List<Customer> readAllCustomers() throws SQLException;
		
		public boolean updateCustomer(Customer user) throws SQLException;
		
		public boolean deleteCustomer(String username)throws SQLException ;
		
}
