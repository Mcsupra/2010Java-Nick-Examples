package MoneyApp.Service;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import MoneyAppPojos.Customer;
import MoneyAppServices.CacheServiceSIM;
import MoneyAppServices.UserSignInServiceImpl;

public class UserSignInTests {

	private UserSignInServiceImpl userSignIn;
	private static CacheServiceSIM<Customer> testCache;
	private Customer testUser1;
	private static HashMap<String,Customer> testMap;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 testMap = new HashMap<String, Customer>();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		userSignIn = new UserSignInServiceImpl();
		testCache = new CacheServiceSIM<Customer>();
		testUser1 = new Customer("Michael","Zide", "Mzide", "rEaLlYcLeVeR", "myemail24@gmail.com", "1234567890");
		//testUser2 = new User("Patrick Robertson", "bmxer4life", "HelloWorldhehe!!", "bmxer4life@hotmail.com", "(465)113-1656");
		
		testMap.put(testUser1.getUsername(), testUser1);
		
		testCache.setCache(testMap);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createUserTest() {
		
		
		Customer userRef = new Customer("Michael","Zide", "M.zide1212", "Ilikespookyghosts123!", "myEmail@gmail.com", "86753099");
		Customer userTest = userSignIn.createUser("Michael", "Zide", "M.zide1212", "Ilikespookyghosts123!", "myEmail@gmail.com", "86753099");
		
		assertEquals(true,userTest.equals(userRef));
	}
	
	

	@Test
	public void signInTest() {	
		
		userSignIn.setUserCache(testCache);
		
		assertTrue("User was found",testMap.containsKey(testUser1.getUsername())
				   ==userSignIn.signIn(testUser1.getUsername(),testUser1.getPassword()));		
	}

	
}
