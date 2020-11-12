package MoneyAppWebServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import MoneyAppDao.BankDaoPostgres;
import MoneyAppDao.CreditDaoPostgres;
import MoneyAppDao.UserDaoPostgres;
import MoneyAppPojos.Bank;
import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;
import MoneyAppServices.CreateMoney;
import MoneyAppServices.UserSignIn;


public class FrontWebImpl implements UserSignIn, CreateMoney {
	
	UserDaoPostgres currentUser = new UserDaoPostgres();
	BankDaoPostgres newBank = new BankDaoPostgres();
	CreditDaoPostgres newCard = new CreditDaoPostgres();
	public static List<Bank> returnedBanks = new ArrayList<>();
	public static List<Credit> returnedCards = new ArrayList<>();
	Customer newCustomer = new Customer();
	private static Logger log = Logger.getLogger("Web");
	
	@Override
	//This method creates a user obj to send to the dao to put into the db
	public boolean createUser(String username, String password, String email, 
			String phoneNum, String firstName, String lastName) {
		
		try {
			//Checking to see if the object is already in the db
			Customer checkCustomer = currentUser.readCustomer(username);
			
			//If the user is not in the system, construct a user object to be stored in the database
			if (checkCustomer == null) {
				newCustomer = new Customer(username,password,email,phoneNum,firstName,lastName);
				currentUser.createCustomer(newCustomer);
				return true;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("SQLException:" + e);
			return false;
		}
		return false;
	}

	@Override
	public int signIn(String username, String password) {
		
		try {
			//Checking to see if the object is already in the db
			Customer checkCustomer = currentUser.readCustomer(username);
			
			if (checkCustomer == null) {
				//If return is -1, user does not exist in DB
				return -1;
			}else {
				if (checkCustomer.getPassword().equals(password))
					//Passwords matched
					return 1;
			}
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return -99;
		}
		//Incorrect password
		return 0;
	}

	@Override
	public int createBank(String bankName, double currentBalance, String accountNumber, String routingNumber, Customer currUser) {

		try {
			//Get a list of banks for the current user
			returnedBanks = newBank.readBank(currUser);
			//If the list of banks is empty, exit
			if (returnedBanks == null) {
				return 10;
			}
			
			for(Bank b:returnedBanks) {
				if (b.getAccountNumber().equals(accountNumber)) {
					//Bank account already exists for this user
					return 0;
				}
			}
				
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return -99;
		}
		
		Bank addBank = new Bank(bankName, currentBalance, accountNumber, routingNumber);
		
		try {
			newBank.createBank(addBank, currUser);
			//Bank created successfully
			return 1;
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return 99;
		}
	}

	@Override
	public int createCredit(String cardNum, String cardType, int expirationDate, int cVV, double balance, Customer currUser) {
		
		try {
			returnedCards = newCard.readCredit(currUser);
			
			for(Credit c:returnedCards) {
				if (c.getCardNum().equals(cardNum)) {
					//Bank account already exists for this user
					return 0;
				}
			}
				
		} catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
		Credit addCredit = new Credit(cardNum, cardType, expirationDate, cVV, balance);
		
		try {
			//Attempt to add the newly created card obj
			newCard.createCredit(addCredit, currUser);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return -99;
		}
		
		//Bank created successfully
		return 1;
	}

}
