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
import MoneyAppServices.MoneyTransferService;



public class TransferWebImpl implements MoneyTransferService {

	public static CreditDaoPostgres creditDao = new CreditDaoPostgres();
	public static UserDaoPostgres userDao = new UserDaoPostgres();
	public static BankDaoPostgres bankDao = new BankDaoPostgres();
	List<Credit> sendCard = new ArrayList<>();
	List<Credit> fromCard = new ArrayList<>();
	List<Credit> allCards = new ArrayList<>();
	List<Bank> allBanks = new ArrayList<>();
	Credit validCard = new Credit();
	Bank validBank = new Bank();
	Customer currentUser = new Customer();
	private static Logger log = Logger.getLogger("Web");
	
	@Override
	public boolean SendMoney(String fromUser, String toUsername, double amount) {
		
		//Amount to send must be positive
		if (amount < 0) {
			return false;
		}
		
		int count = 0;
		Customer toCustomer;
		Customer fromCustomer;
		
		try {
			
			//Use the username to receive a customer and their cards
			fromCustomer = userDao.readCustomer(fromUser);
			fromCard = creditDao.readCredit(fromCustomer);
			
			//TODO : Logic currently just selects a random valid card
			for (int i = 0; i < fromCard.size();i++) {
				if (fromCard.get(i).getBalance() > amount){
					validCard = fromCard.get(i);
					count++;
					break;
				}
			} 
			
			//If not cards were found then we return
			if (count == 0)
				return false;
			
			//Use the username to receive a customer and their cards
			toCustomer = userDao.readCustomer(toUsername);
			sendCard = creditDao.readCredit(toCustomer);
			if (sendCard == null)
				return false;
			
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return false;
		}
		
		
		//Attempt to send the amount from one user to another
		try {
			return creditDao.SendMoneyProcedure(validCard.getCardNum(), sendCard.get(0).getCardNum(), amount);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return false;
		}		
	}

	@Override
	public boolean AddFunds(String username, double amount) {
		
		//Amount to send must be positive
		int count = 0;
		if (amount < 0) {
			return false;
		}
		
		try {
			//Looks at a specific user and attempt to return all their banks and cards
			currentUser = userDao.readCustomer(username);
			allBanks = bankDao.readBank(currentUser);
			allCards = creditDao.readCredit(currentUser);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
		//If either list is empty, exit out
		if (allCards == null || allBanks == null)
			return false;
		
		//TODO : Logic currently just selects a random valid bank
		for (int i = 0; i < allBanks.size();i++) {
			if (allBanks.get(i).getCurrentBalance()>amount) {
				validBank = allBanks.get(i);
				count++;
				break;
			}
		}
		
		//If there are no items that have enough of a balance 
		if (count == 0)
			return false;
		
		try {
			//Attempt to withdraw funds from bank to card
			return creditDao.AddFundsProcedure(validBank.getAccountNumber(), allCards.get(0).getCardNum(), amount);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return false;
		}
		
	}

	@Override
	public boolean Removefunds(String username, double amount) {
		
		//Amount to send must be positive
		int count = 0;
		if (amount < 0) {
			return false;
		}
		
		try {
			//Looks at a specific user and attempt to return all their banks and cards
			currentUser = userDao.readCustomer(username);
			allBanks = bankDao.readBank(currentUser);
			allCards = creditDao.readCredit(currentUser);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
		//If either list is null, exit immediately
		if (allBanks == null || allCards == null)
			return false;
		
		//TODO : Logic currently just selects a random valid card
		for (int i = 0; i < allCards.size();i++) {
			if (allCards.get(i).getBalance()>amount) {
				validCard = allCards.get(i);
				count++;
				break;
			}
		}
		
		//If no valid banks are found, exit
		if (count == 0)
			return false;
		
		try {
			//Attempt to deposit funds from card to bank
			return bankDao.RemovedFundsProcedure(validCard.getCardNum(), allBanks.get(0).getAccountNumber(),amount);
		} catch (SQLException e) {
			log.error("SQLException:" + e);
			return false;
		}

	}

}
