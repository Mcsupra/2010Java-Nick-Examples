package MoneyApp.Service;

import static org.junit.Assert.*;
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
import MoneyAppDao.BankDaoPostgres;

import MoneyAppPojos.Bank;
import MoneyAppPojos.Customer;

@RunWith(MockitoJUnitRunner.class)
public class BankDaoTest {
	
	private static Logger log = Logger.getLogger("Web");
	public static BankDaoPostgres bankDao = new BankDaoPostgres();
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
	private static Connection realConnection;
	
	public static Bank testBank; 
	public static Customer testUser;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		realConnection = new ConnectionUtil().createConnection();
		testBank = new Bank("Bank of America",16873.24 ,"45857565578","091000019");
		testUser = new Customer("gobbleking","Notmydrumstick","giblets@turkeytownnet.com","9018675309","Turkey","McDurkey");
		
		String sql = "insert into customer(username,passphrase,email,phone_num,first_name,last_name)"
				+" values ('gobbleking','Notmydrumstick','giblets@turkeytownnet.com','9018675309','Turkey','McDurkey');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
	}

	@After
	public void tearDown() throws Exception {
		
		stmt = realConnection.prepareStatement("delete from customer where username = 'gobbleking';");
		stmt.executeUpdate();
		
		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void createBankTest() throws SQLException {
		//Prepared SQL statement prototype
		String sql = "insert into bank (bank_name,current_balance,account_number,routing_number,username) "
				+ "values(?,?,?,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		
		try {
			bankDao.createBank(testBank,testUser);
			
			verify(spy).setString(1, testBank.getBankName());
			verify(spy).setDouble(2, testBank.getCurrentBalance());
			verify(spy).setString(3, testBank.getAccountNumber());
			verify(spy).setString(4, testBank.getRoutingNumber());
			verify(spy).setString(5, testUser.getUsername());
			
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from bank where account_number = '45857565578';");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added bank");
			}
		}	
		
	}
	
	@Test
	public void readBankTest() throws SQLException {
		
		String sql = "insert into bank (bank_name,current_balance,account_number,routing_number,username) "
				+ "values('Bank of America',16873.24 ,'45857565578','091000019','gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement prototype
		sql = "select * from bank where username = ?";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			List<Bank> allBanks = new ArrayList<>();
			allBanks = bankDao.readBank(testUser);
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).executeQuery();
			
			assertEquals(true, allBanks.get(0).equals(testBank));
			
		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from bank where username = 'gobbleking';");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added bank");
			}
		}	
		
	}
	
	
	@Test
	public void UpdateBankTest() throws SQLException {
		
		String sql = "insert into bank (bank_name,current_balance,account_number,routing_number,username) "
				+ "values('Bank of America',16873.24 ,'45857565578','091000019','gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement
		sql = "UPDATE bank SET current_balance = ? WHERE username = ? and account_number = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testBank.setCurrentBalance(15324.67);
			
			bankDao.updateBank(testBank, testUser);
			
			verify(spy).setDouble(1, testBank.getCurrentBalance());
			verify(spy).setString(2, testUser.getUsername());
			verify(spy).setString(3, testBank.getAccountNumber());
			
			verify(spy).executeUpdate();
			
			//Pull modified player object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM bank WHERE account_number = ?;");
			stmt.setString(1, testBank.getAccountNumber());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			Bank bankMod = new Bank(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4));

			assertEquals("Database object does not match as modified", testBank, bankMod);
			
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from bank where account_number = '45857565578'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQL Exception:" + e);
				fail("Error: Could not remove added bank");
			}
		}	

	}
	
	@Test
	public void DeleteBankTest() throws SQLException {
		
		String sql = "insert into bank (bank_name,current_balance,account_number,routing_number,username) "
				+ "values('Bank of America',16873.24 ,'45857565578','091000019','gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL Statement
		sql = "delete from bank where username = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQL Exception:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		
		
		try {
			bankDao.deleteBank(testUser);
			
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
		
		bankDao.setConnUtil(connUtil);
		
		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}

}


