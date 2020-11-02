package MoneyAppFlowLogic;

import java.util.Scanner;

import MoneyAppPojos.Bank;
import MoneyAppPojos.Credit;
import MoneyAppPojos.User;
import MoneyAppServices.CacheServiceSIM;
import MoneyAppServices.CreateMoneyImpl;
import MoneyAppServices.MoneyTransferServiceImpl;
import MoneyAppServices.UserSignInServiceImpl;

public class MoneyAppControllerImpl implements MoneyAppController  {
	
	UserSignInServiceImpl userSignInChoice = new UserSignInServiceImpl();
	MoneyTransferServiceImpl betweenUsers = new MoneyTransferServiceImpl();
	MoneyTransferServiceImpl addFunds = new MoneyTransferServiceImpl();
	MoneyTransferServiceImpl remFunds = new MoneyTransferServiceImpl();
	User currentUser = new User();
	Bank currentBank = new Bank();
	CacheServiceSIM<Bank> sentBank = new CacheServiceSIM<>();
	Credit currentCredit = new Credit();
	CacheServiceSIM<Credit> sentCredit = new CacheServiceSIM<>();
	String cardNum;
	String acctNum;
	CreateMoneyImpl cacheBankCredit = new CreateMoneyImpl(sentBank,sentCredit);
	
	private static Scanner scan = new Scanner(System.in);
	
	@Override
	public boolean callUserSignIn(int decision) {
		switch(decision) {
		
			case 1:
			//User wants to create a new user
				System.out.println("What is your full name?");
				String name = scan.nextLine();
				System.out.println("What do you want your username to be?");
				String username = scan.nextLine();
				System.out.println("What do you want your password to be?");
				String password = scan.nextLine();
				System.out.println("What is your email?");
				String email = scan.nextLine();
				System.out.println("What is your phone number?");
				String phoneNumber = scan.nextLine();
				
				return(userSignInChoice.createUser(name, username, password, email, phoneNumber)!=null);
				
			case 2:
				System.out.println("What is your username?");
				String currentUsername = scan.nextLine();
				System.out.println("What is you password?");
				String currentPassword = scan.nextLine();
				return (userSignInChoice.signIn(currentUsername, currentPassword));
				
			default:
				return false;
		}
	}

	@Override
	public boolean callMoneyTransferService(int decision) {
		
		switch(decision) {
			case 1:
				System.out.println("What is the name of your bank?");
				String bankName = scan.nextLine();
				System.out.println("Please enter a bank account number:");
				acctNum = scan.nextLine();
				System.out.println("Please enter a routing number:");
				String routeNum = scan.nextLine();
				System.out.println("Set your current balance:");
				double balance = scan.nextDouble();
				scan.nextLine();
				
				currentBank= cacheBankCredit.createBank(bankName, balance, acctNum, routeNum);
				return (currentBank != null);
			
			case 2:
				System.out.println("What type of card is this?");
				String cardType = scan.nextLine();
				System.out.println("Please enter your card number:");
				cardNum = scan.nextLine();
				System.out.println("Please enter the expiration date:");
				int expDate = scan.nextInt();
				scan.nextLine();
				System.out.println("Please enter the CVV:");
				int cVV = scan.nextInt();
				scan.nextLine();
				System.out.println("Set your current balance:");
				double cardBalance = scan.nextDouble();
				scan.nextLine();
				
				currentCredit = cacheBankCredit.createCredit(cardNum, cardType, expDate, cVV,cardBalance);
				return (currentCredit != null);
			
			case 3:	
				System.out.println("Who is sending the money?");
				String sendMoneyFrom = scan.nextLine();
				
				System.out.println("Who is receiving the money?");
				String sendMoneyTo = scan.nextLine();
				
				System.out.println("How much would you like to send?");
				double amountToSend = scan.nextInt();
				scan.nextLine();
				
				try{
					return(betweenUsers.SendMoney(cacheBankCredit.getCreditCache().retrieveItemFromCache(sendMoneyFrom), 
							cacheBankCredit.getCreditCache().retrieveItemFromCache(sendMoneyTo), amountToSend));
				}catch (NullPointerException e) {
					return false;
				}
			case 4:	
				System.out.println("Select bank to withdraw from:");
				String moneyFromBank = scan.nextLine();
				
				System.out.println("Select which card to send to");
				String moneyToCard = scan.nextLine();
				
				System.out.println("How much would you like to withdraw?");
				double amountToWithdraw= scan.nextInt();
				scan.nextLine();
				
				try{
					return(betweenUsers.AddFunds(cacheBankCredit.getBankCache().retrieveItemFromCache(moneyFromBank), 
							cacheBankCredit.getCreditCache().retrieveItemFromCache(moneyToCard), amountToWithdraw));
				}catch (NullPointerException e) {
					return false;
				}
			case 5:	
				System.out.println("Select card to withdraw from:");
				String moneyFromCard = scan.nextLine();
				
				System.out.println("Select which bank to send to");
				String moneyToBank= scan.nextLine();
				
				System.out.println("How much would you like to withdraw?");
				double amountToDeposit= scan.nextInt();
				scan.nextLine();
				
				try{
					return(betweenUsers.AddFunds(cacheBankCredit.getBankCache().retrieveItemFromCache(moneyFromCard), 
							cacheBankCredit.getCreditCache().retrieveItemFromCache(moneyToBank), amountToDeposit));
				}catch (NullPointerException e) {
					return false;
				}
			case 6:
				System.out.println("Which card do you want to check the balance of?");
				String creditBalance = scan.nextLine();
				
				if (cacheBankCredit.getCreditCache().retrieveItemFromCache(creditBalance)!=null) {
					System.out.println(cacheBankCredit.getCreditCache().retrieveItemFromCache(creditBalance).getBalance());
					return true;
				}
				else
					return false;
			case 7:
				System.out.println("Which card do you want to check the balance of?");
				String bankBalance = scan.nextLine();
				
				if (cacheBankCredit.getBankCache().retrieveItemFromCache(bankBalance)!=null) {
					System.out.println(cacheBankCredit.getBankCache().retrieveItemFromCache(bankBalance).getCurrentBalance());
					return true;
				}
				else
					return false;	
			default:
				return false;
		}
	}
}
