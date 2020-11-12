package MoneyAppServices;

import MoneyAppPojos.Bank;
import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;

public interface MoneyTransferService {
	
	public boolean SendMoney(String fromUser, String toUsername ,double amount);
	
	public boolean AddFunds(String currentUsername, double amount);
	
	public boolean Removefunds(String currentUsername, double amount);
	
}
