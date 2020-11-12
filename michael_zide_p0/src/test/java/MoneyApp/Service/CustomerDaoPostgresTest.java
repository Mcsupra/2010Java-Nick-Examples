package MoneyApp.Service;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ConnectionUtil.ConnectionUtil;
import MoneyAppDao.UserDaoPostgres;
import MoneyAppPojos.Customer;

@RunWith(MockitoJUnitRunner.class)
public class CustomerDaoPostgresTest {
	
	private static Logger log = Logger.getLogger("Web");
	public static UserDaoPostgres userDao = new UserDaoPostgres();
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
	private static Connection realConnection;
	
	public static Customer testUser = new Customer(); 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		realConnection = new ConnectionUtil().createConnection();
		testUser = new Customer("gobbleking","Notmydrumstick","giblets@turkeytownnet.com","9018675309","Turkey","McDurkey");
		
		
	}

	@After
	public void tearDown() throws Exception {
		
		
		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void createCustomerTest() throws SQLException {
		//Prepared SQL statement prototype
		String sql = "insert into customer (username,passphrase,email,phone_num,first_name,last_name) "
				+ "values(?,?,?,?,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		
		try {
			userDao.createCustomer(testUser);
			
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).setString(2, testUser.getPassword());
			verify(spy).setString(3, testUser.getEmail());
			verify(spy).setString(4, testUser.getPhoneNum());
			verify(spy).setString(5, testUser.getFirstName());
			verify(spy).setString(6, testUser.getLastName());
			
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from customer where first_name = 'Turkey' AND last_name = 'McDurkey'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added customer");
			}
		}	
		
	}
	
	@Test
	public void readCustomerTest() throws SQLException {
		
		String sql = "insert into customer(username,passphrase,email,phone_num,first_name,last_name)"
				+" values ('gobbleking','Notmydrumstick','giblets@turkeytownnet.com','9018675309','Turkey','McDurkey');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement prototype
		sql = "select * from customer where username = ?";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			
			Customer testCustomer = userDao.readCustomer(testUser.getUsername());
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).executeQuery();
			
			assertEquals(true, testCustomer.equals(testUser));
			
		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from customer where first_name = 'Turkey' AND last_name = 'McDurkey'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added customer");
			}
		}	
		
	}
	
	@Test
	public void ReadAllCustomerTest() throws SQLException {
		
		List<Customer> testList= new ArrayList<>(); 
		
		//Baseline of test is number in table
		String sql = "select count(*) from customer";
		int num_in_table = -1;

		try {
			stmt = realConnection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			num_in_table = rs.getInt(1);
			//TODO add log
		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			System.out.println("SQLException thrown: " + e.toString());
		}
		
		
		//Prepared SQL statement prototype*/
		sql = "select * from customer";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testList = userDao.readAllCustomers();
			
			verify(spy).executeQuery();
			if (testList.size() != num_in_table) {
				fail("Error: Queried data does not match current DB config");
			}
				
			for (Customer c: testList) {
				assertFalse("Non-nullable username is null","".equals(c.getUsername()));
				assertFalse("Non-nullable passphrase is null","".equals(c.getPassword()));
				assertFalse("Non-nullable first_name is null","".equals(c.getFirstName()));
				assertFalse("Non-nullable last_name is null","".equals(c.getLastName()));
			}

			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	@Test
	public void UpdateCustomerTest() throws SQLException {
		
		String sql = "insert into customer(username,passphrase,email,phone_num,first_name,last_name)"
				+" values ('gobbleking','Notmydrumstick','giblets@turkeytownnet.com','9018675309','Turkey','McDurkey');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement
		sql = "UPDATE customer SET passphrase = ?, " 
    			+ "email = ?, phone_num = ?, first_name = ?, last_name = ? "
    			+ "WHERE username = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testUser.setPassword("ChristmasistheBest!");
			testUser.setPhoneNum("9017573212");
			testUser.setEmail("SantaClaus@northpole.org");
			testUser.setFirstName("Kris");
			testUser.setLastName("Kringle");
			
			userDao.updateCustomer(testUser);
			
			verify(spy).setString(1, testUser.getPassword());
			verify(spy).setString(2, testUser.getEmail());
			verify(spy).setString(3, testUser.getPhoneNum());
			verify(spy).setString(4, testUser.getFirstName());
			verify(spy).setString(5, testUser.getLastName());
			verify(spy).setString(6, testUser.getUsername());
			
			verify(spy).executeUpdate();
			
			//Pull modified customer object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM customer WHERE username = ?;");
			stmt.setString(1, testUser.getUsername());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			Customer customerMod = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));

			assertEquals("Database object does not match as modified", testUser, customerMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from customer where username = 'gobbleking'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added customer");
			}
		}	

	}
	
	@Test
	public void DeleteCustomerTest() throws SQLException {
		
		String sql = "insert into customer(username,passphrase,email,phone_num,first_name,last_name)"
				+" values ('gobbleking','Notmydrumstick','giblets@turkeytownnet.com','9018675309','Turkey','McDurkey');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL Statement
		sql = "delete from customer where username = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		
		
		try {
			userDao.deleteCustomer(testUser.getUsername());
			
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setString(1, testUser.getUsername());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		userDao.setConnUtil(connUtil);
		
		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}

}
