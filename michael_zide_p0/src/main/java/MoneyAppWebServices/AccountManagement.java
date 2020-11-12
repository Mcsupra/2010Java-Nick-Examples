package MoneyAppWebServices;

import MoneyAppDao.BankDaoPostgres;
import MoneyAppDao.CreditDaoPostgres;
import MoneyAppDao.UserDaoPostgres;

public class AccountManagement {
	
	public static CreditDaoPostgres creditDao = new CreditDaoPostgres();
	public static UserDaoPostgres userDao = new UserDaoPostgres();
	public static BankDaoPostgres bankDao = new BankDaoPostgres();
	
	
	

}
