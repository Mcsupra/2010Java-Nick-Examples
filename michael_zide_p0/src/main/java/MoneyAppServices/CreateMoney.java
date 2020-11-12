package MoneyAppServices;

import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;

public interface CreateMoney {
	
	public int createBank(String bankName, double currentBalance, String accountNumber, String routingNumber, Customer currentUser);
	
	public int createCredit(String cardNum, String cardType, int expirationDate, int cVV, double balance, Customer currentUser);
}
