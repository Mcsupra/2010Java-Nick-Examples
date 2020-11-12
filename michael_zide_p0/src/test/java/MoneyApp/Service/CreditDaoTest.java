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
import MoneyAppDao.CreditDaoPostgres;
import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;

@RunWith(MockitoJUnitRunner.class)
public class CreditDaoTest {

	private static Logger log = Logger.getLogger("Web");
	public static CreditDaoPostgres creditDao = new CreditDaoPostgres();
	
	@Mock
	private static ConnectionUtil connUtil;
	
	@Mock
	private static Connection fakeConn;
	
	private static PreparedStatement stmt;
	
	private static PreparedStatement spy;
	
	private static Connection realConnection;
	
	public static Credit testCredit = new Credit(); 
	
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
		testCredit = new Credit("4111111111111111", "VISA", 1221, 118, 1234.56);
		testUser = new Customer("gobbleking","Notmydrumstick","giblets@turkeytownnet.com","9018675309","Turkey","McDurkey");
		
	}

	@After
	public void tearDown() throws Exception {
		
		
		if (realConnection != null) {
			realConnection.close();
		}
		
	}

	@Test
	public void createCreditTest() throws SQLException {
		//Prepared SQL statement prototype
		String sql = "insert into credit (card_num,card_type,exp_date,cvv,balance,username) "
				+ "values(?,?,?,?,?,?);";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		
		try {
			creditDao.createCredit(testCredit, testUser);
			
			verify(spy).setString(1, testCredit.getCardNum());
			verify(spy).setString(2, testCredit.getCardType());
			verify(spy).setInt(3, testCredit.getExpirationDate());
			verify(spy).setInt(4, testCredit.getCVV());
			verify(spy).setDouble(5, testCredit.getBalance());
			verify(spy).setString(6, testUser.getUsername());
			
			verify(spy).executeUpdate();

		}catch (SQLException e) {
			log.error("SQLException:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from credit where username = 'gobbleking';");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQLException:" + e);
				fail("Error: Could not remove added card");
			}
		}
		
	}
	
	@Test
	public void readCreditTest() throws SQLException {
		
		String sql = "insert into credit (card_num,card_type,exp_date,cvv,balance,username) "
				+" values ('4111111111111111','VISA',1221, 118, 1234.56,'gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement prototype
		sql = "select * from credit where username = ?";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			List<Credit> allCards = new ArrayList<>();
			allCards = creditDao.readCredit(testUser);
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).executeQuery();
			
			assertEquals(true, allCards.get(0).equals(testCredit));
			
		}catch (SQLException e) {
			log.error("SQLException:" + e);
			fail("Exception thrown: " + e);
		}finally {
			try {
				stmt = realConnection.prepareStatement("delete from credit where card_num = '4111111111111111'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQLException:" + e);
				fail("Error: Could not find card");
			}
		}	
		
	}
	
	
	@Test
	public void UpdateCreditTest() throws SQLException {
		
		String sql = "insert into credit (card_num,card_type,exp_date,cvv,balance,username) "
				+" values ('4111111111111111','VISA',1221, 118, 1234.56,'gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL statement
		sql = "UPDATE credit SET balance = ? WHERE username = ? and card_num = ?;";
		
		try {
			preparedHelper(sql);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			testCredit.setBalance(754.56);
			
			creditDao.updateCredit(testCredit,testUser);
			
			verify(spy).setDouble(1, testCredit.getBalance());
			verify(spy).setString(2, testUser.getUsername());
			verify(spy).setString(3, testCredit.getCardNum());
			
			verify(spy).executeUpdate();
			
			//Pull modified player object from database for comparison
			stmt = realConnection.prepareStatement("SELECT * FROM credit WHERE username = ? and card_num = ?;");
			stmt.setString(1, testUser.getUsername());
			stmt.setString(2, testCredit.getCardNum());
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			
			Credit creditMod = new Credit(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5));
			assertEquals("Database object does not match as modified", testCredit, creditMod);
			
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		finally {
			try {
				stmt = realConnection.prepareStatement("delete from credit where card_num = '4111111111111111'");
				stmt.executeUpdate();
			}catch(SQLException e) {
				log.error("SQLException:" + e);
				fail("Error: Could not remove added card");
			}
		}
	}
	
	@Test
	public void DeleteCreditTest() throws SQLException {
		
		String sql = "insert into credit (card_num,card_type,exp_date,cvv,balance,username) "
				+" values ('4111111111111111','VISA',1221, 118, 1234.56,'gobbleking');";
		
		stmt = realConnection.prepareStatement(sql);
		stmt.executeUpdate();
		
		//Prepared SQL Statement
		sql = "delete from credit where username = ?;";
		
		try {
			preparedHelper(sql);
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("SQLException thrown: " + e.toString());
		}
		
		try {
			creditDao.deleteCredit(testUser);
			
			verify(spy).setString(1, testUser.getUsername());
			verify(spy).executeUpdate();
			
			stmt = realConnection.prepareStatement(sql);
			stmt.setString(1, testUser.getUsername());
			assertEquals("Object was not deleted properly", 0, stmt.executeUpdate());
			
		}catch(SQLException e) {
			log.error("SQLException:" + e);
			fail("Exception thrown: " + e);
		}
		
	}
	
	
	private void preparedHelper(String sql) throws SQLException {
		
		creditDao.setConnUtil(connUtil);
		
		//creating a real stmt from a connection
		stmt = realConnection.prepareStatement(sql); 
		
		//spying on that real stmt
		spy = Mockito.spy(stmt);
		
		//mock our connection and util, so we will only use the stmt we are spying on
		when(connUtil.createConnection()).thenReturn(fakeConn);
		when(fakeConn.prepareStatement(sql)).thenReturn(spy);
		
		
	}

}


