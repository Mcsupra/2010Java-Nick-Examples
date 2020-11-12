package MoneyAppController;

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
import MoneyAppWebServices.FrontWebImpl;
import MoneyAppWebServices.TransferWebImpl;
import io.javalin.http.Context;

public class WebController {
	
	FrontWebImpl dummy = new FrontWebImpl();
	TransferWebImpl runner = new TransferWebImpl();
	UserDaoPostgres checkUser = new UserDaoPostgres();
	CreditDaoPostgres card = new CreditDaoPostgres();
	BankDaoPostgres bank = new BankDaoPostgres();
	Customer checkedUser = new Customer();
	List<Credit> allCards = new ArrayList<>();
	List<Bank> allBanks = new ArrayList<>();
	
	private static Logger log = Logger.getLogger("Web");
	public static int status;
	
	public void callCreateUser(Context ctx){
		
		 //User input for username
        String username = ctx.formParam("username");
        
        //User input for password
        String password = ctx.formParam("password");
        
        //User input for email
        String email = ctx.formParam("email");
        
     	//User input for phone number
        String phoneNum = ctx.formParam("phoneNum");
        
        //User input for first
        String firstName = ctx.formParam("firstName");
        
        //User input for last
        String lastName = ctx.formParam("lastName");
        
        //Pass in customer creation params to service that constructs the customer object
        //If the return was false then the customer is not created
       if (dummy.createUser(username, password, email, phoneNum, firstName, lastName)) {
    	   log.info("User was successfully created");
    	   ctx.status(201);
    	   ctx.html("User creation was successful");
       }
       else {
    	   //If the return was false then the customer is not created
    	   log.warn("User was unsuccessfully created");
    	   ctx.status(406);
    	   ctx.html("User creation was unsuccessful - username already exists or information was missing");
       }
	}
	
	public void callSignIn(Context ctx){
		
		 //User input for username
        String username = ctx.formParam("username");
        
        //User input for password
        String password = ctx.formParam("password");
        
        //Receives the results on the sign in service which used a switch to determine next action
        status = dummy.signIn(username, password);
        
        switch (status) {
        case -1:
        	log.info("User tried to log in but the account is not in the system");
        	ctx.status(406);
        	ctx.html("User does not exist in database - please create an account first");
        	break;
        case 0:
        	log.info("User enetered the wrong password");
        	ctx.status(401);
        	ctx.html("The password entered was not correct");
        	break;
        case 1:
        	log.info("User successfully logged in");
        	ctx.status(200);
        	ctx.html("You have successfully logged it!");
        	break;
        default:
        	log.warn("User tried to log in but the account is not in the system");
        	ctx.status(404);
        	ctx.html("You were not signed in. Please try again");
        	break;
        }
		
	}
	
	public void callCreateBank(Context ctx){
		
		 //User input for bank name
        String bankName = ctx.formParam("bankName");
        
        //User input for balance
        String currentBal = ctx.formParam("currentBalance");
        double currentBalance = Double.parseDouble(currentBal); 
        
        //User input for account #
        String accountNumber = ctx.formParam("accountNumber");
        
        //User input for routing #
        String routingNumber = ctx.formParam("routingNumber");
        
        //User input for username
        String username = ctx.formParam("username");
        
        //User input for password
        String password = ctx.formParam("password");
        
        //User input for email
        String email = ctx.formParam("email");
        
     	//User input for phone number
        String phoneNum = ctx.formParam("phoneNum");
        
        //User input for first
        String firstName = ctx.formParam("firstName");
        
        //User input for last
        String lastName = ctx.formParam("lastName");
        
        //Construct a user object to use with the method
        Customer currUser = new Customer(username, password, email, phoneNum, firstName, lastName);
        
        try {
        	//Check to see if the user is in the database
			checkedUser = checkUser.readCustomer(currUser.getUsername());
			//If not, immediately quit
			if (checkedUser == null) {
	        	status = 70;
	        } 
			//Attempt to create a bank
	        else {
				status = dummy.createBank(bankName, currentBalance, accountNumber, routingNumber, currUser);
			}	
			
			//Print out various responses depending on the reply
			switch (status) {
	        case 0:
	        	log.info("User tried to create a bank that already existed");
	        	ctx.status(406);
	        	ctx.html("Bank account already exists in the database");
	        	break;
	        case 1:
	        	log.info("User successfully created a bank");
	        	ctx.status(201);
	        	ctx.html("Bank successfully added");
	        	break;
	        case 70:
	        	log.warn("Request was sent to create a bank for a username not in the database");
	        	ctx.status(404);
	        	ctx.html("Username not in the system");
	        	break;
	        case -99:
	        	log.error("Exception was caught");
	        	ctx.status(500);
	        	ctx.html("Server error");
	        default:
	        	ctx.status(400);
	        	log.info("User enetered the wrong password");
	        	ctx.html("Error in bank creation - try again");
	        	break;
	        }    
        }catch (SQLException e) {
        	log.error("SQL Exception" + e);
		}	
	}
	
	public void callCreateCredit(Context ctx){
		
		//User input for card number
        String cardNum = ctx.formParam("cardNum");
        
        //User input for card type
        String cardType = ctx.formParam("cardType");
        
     	//User input for exp date
        String expDate = ctx.formParam("expirationDate");
        int expirationDate = Integer.parseInt(expDate);
        
        //User input for CVV
        String CVV = ctx.formParam("cVV");
        int cVV = Integer.parseInt(CVV);
        
        //User input for balance
        String bal = ctx.formParam("balance");
        double balance = Double.parseDouble(bal);
        
        //User input for username
        String username = ctx.formParam("username");
        
        //User input for password
        String password = ctx.formParam("password");
        
        //User input for email
        String email = ctx.formParam("email");
        
     	//User input for phone number
        String phoneNum = ctx.formParam("phoneNum");
        
        //User input for first
        String firstName = ctx.formParam("firstName");
        
        //User input for last
        String lastName = ctx.formParam("lastName");
        
        //Construct a user object to use with the method
        Customer currUser = new Customer(username, password, email, phoneNum, firstName, lastName);
        
        try {
        	//Check to see if the user is in the database
			checkedUser = checkUser.readCustomer(currUser.getUsername());
		
		//Username does not exists in the database
        if (checkedUser == null) {
        	status = 70;
        } 
        //Card number validation using luhn's
        else if (LuhnsFull.LuhnsAlgo(cardNum) != 0) {
        	status = dummy.createCredit(cardNum, cardType, expirationDate, cVV, balance, currUser);
        }
        //If Luhn's fails, send in a fail status
        else {
        	status = -99;
        }
        	//Responce with the correct status code
	        switch (status) {
	        case 0:
	        	log.info("User tried to create a card that already existed in database");
	        	ctx.status(406);
	        	ctx.html("Card already exists in the database");
	        	break;
	        case 1:
	        	log.info("User successfully added a card to the database");
	        	ctx.status(201);
	        	ctx.html("Card successfully added");
	        	break;
	        case 70:
	        	log.warn("Username not in the database");
	        	ctx.status(404);
	        	ctx.html("User doesn't exist");
	        	break;
	        case -99:
	        	log.info("Users input failed luhn's");
	        	ctx.status(404);
	        	ctx.html("Not a valid card number");
	        	break;
	        default:
	        	log.warn("User encountered an error in the code");
	        	ctx.status(400);
	        	ctx.html("Error in card creation - try again");
	        	break;
	        }
			 
		}catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}
	}
	
	public void callSendMoney(Context ctx){
		       
        //User input for username
        String username = ctx.formParam("username");
       
        //User input for an amount
        String sendAmount = ctx.formParam("amount");
        double amount = Double.parseDouble(sendAmount);
        
        //User input for username to send to
        String toUsername = ctx.formParam("toUsername");
      
        //Send requests with an amount handed down and printed out if successful
        if (runner.SendMoney(username, toUsername, amount)) {
        	log.info("User successfully sent money");
        	ctx.status(200);
        	ctx.html("You have successfully sent $"
        			+ amount 
        			+" to "
        			+ toUsername);
        }
        else {
        	log.warn("User failed to send money");
        	ctx.status(400);
        	ctx.html("Send money failed - check the username, your available funds, or a valid amout to send");
        }
		
	}
	
	public void callAddFunds(Context ctx) {
		
		//User input for username
        String username = ctx.formParam("username");
       
        //User input for an amount
        String sendAmount = ctx.formParam("amount");
        double amount = Double.parseDouble(sendAmount);
        
        //User, if successful, withdraws money from their bank to store on card
        if (runner.AddFunds(username, amount)) {
        	log.info("User successfully added money to card from bank");
        	ctx.status(200);
        	ctx.html("You have successfully added $"
        			+ amount
        			+" to your card");
        }
        else {
        	log.warn("User tried to withdraw money from bank but failed");
        	ctx.status(400);
        	ctx.html("There was an error adding funds. Check to make sure there are enough funds in your bank");
        }
	}
	
	public void callRemoveFunds(Context ctx) {
		
		//User input for username
        String username = ctx.formParam("username");
       
        //User input for an amount
        String sendAmount = ctx.formParam("amount");
        double amount = Double.parseDouble(sendAmount);
        
        //User, if successful, deposits money from their card to their bank
        if (runner.Removefunds(username, amount)) {
        	log.info("User successfully added money to bank from card");
        	ctx.status(200);
        	ctx.html("You have successfully sent $"
        			+ amount
        			+" to your bank");
        }
        else {
        	log.warn("User tried to deposit money from bank but failed");
        	ctx.status(400);
        	ctx.html("There was an error removing funds. Check to make sure there are enough funds on your card");
        }
	}
	
	public void CallAllBanks (Context ctx) {
		
		//Pick the user that you want to check all balances of
		String username = ctx.formParam("username");
		
		try {
			checkedUser = checkUser.readCustomer(username);
			if (checkedUser != null) {
				allBanks = bank.readBank(checkedUser);
				if (allBanks != null) {
					System.out.println(allBanks);
					ctx.status(200);
					
						//ctx.html("<p> "+b.getAccountNumber()+" "+b.getCurrentBalance()+" </p>");
						ctx.html(allBanks.toString().replaceAll("[,]","\n"));
				}
				else {
					ctx.status(400);
					ctx.html("No banks found for that username");
				}
				
			}
			else {
				ctx.status(400);
				ctx.html("No customer found for that username");
			}	
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}	
	}
	
	public void CallAllCards (Context ctx) {
		
		String username = ctx.formParam("username");
		
		try {
			checkedUser = checkUser.readCustomer(username);
			if (checkedUser != null) {
				allCards = card.readCredit(checkedUser);
				if (allCards != null) {
					ctx.status(200);
					ctx.html(allCards.toString().replaceAll(",","\n"));
				}
				else {
					ctx.status(400);
					ctx.html("No cards found for that username");
				}
			}
			else {
				ctx.status(400);
				ctx.html("No customer found for that username");
			}	
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}
		//test
	}
	
	public void CallDeleteBank (Context ctx) {
		
		String username = ctx.formParam("username");
		
		try {
			checkedUser = checkUser.readCustomer(username);
			
			if (checkedUser != null) {
				allBanks = bank.readBank(checkedUser);
				if (allBanks != null) {
					bank.deleteBank(username);
					ctx.status(200);
					ctx.html("Banks successfully deleted");
				}
				}else {
					ctx.status(200);
					ctx.html("There are no banks to delete");
				}
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}
	}
	
	public void CallDeleteCard (Context ctx) {
		
		String username = ctx.formParam("username");
		
		try {
			checkedUser = checkUser.readCustomer(username);
			if (checkedUser != null) {
				allCards = card.readCredit(checkedUser);
				if (allCards != null) {
					card.deleteCredit(username);
					ctx.status(200);
					ctx.html("Cards successfully deleted");
				}
				}else {
					ctx.status(200);
					ctx.html("There are no cards to delete");
				}
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}
		
	}
	
	public void CallDeleteCustomer (Context ctx) {
		
		String username = ctx.formParam("username");
		
		try {
			checkedUser = checkUser.readCustomer(username);
			if (checkedUser != null) {
				card.deleteCredit(username);
				bank.deleteBank(username);
				checkUser.deleteCustomer(username);
				ctx.status(200);
				ctx.html("Customer successfully deleted");
			}
			else {
				ctx.status(406);
				ctx.html("Customer does not exist");
			}
		} catch (SQLException e) {
			log.error("SQL Exception:" + e);
		}
		
	}
}
