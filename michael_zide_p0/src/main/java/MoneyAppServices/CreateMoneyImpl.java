package MoneyAppServices;

import MoneyAppPojos.Bank;
import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;


public class CreateMoneyImpl implements CreateMoney {
	
	private CacheServiceSIM<Bank> bankCache = new CacheServiceSIM<Bank>();
	private CacheServiceSIM<Credit> creditCache = new CacheServiceSIM<Credit>();

	public CreateMoneyImpl(CacheServiceSIM<Bank> bankCache, CacheServiceSIM<Credit> creditCache) {
		super();
		this.bankCache = bankCache;
		this.creditCache = creditCache;
	}
	
	public CreateMoneyImpl() {
		super();
	}


	public CacheServiceSIM<Bank> getBankCache() {
		return bankCache;
	}

	public void setBankCache(CacheServiceSIM<Bank> bankCache) {
		this.bankCache = bankCache;
	}

	public CacheServiceSIM<Credit> getCreditCache() {
		return creditCache;
	}

	public void setCreditCache(CacheServiceSIM<Credit> creditCache) {
		this.creditCache = creditCache;
	}
	
	/**
	 * Creates a bank obj 
	 * String bankName, double currentBalance, String accountNumber, String routingNumber
	 * Stores it to a bank cache if key is unique
	 * Need to update key to be username
	 * @return Bank
	 */
	@Override
	public int createBank(String bankName, double currentBalance, String accountNumber, String routingNumber) {
	
		Bank newBank = new Bank(bankName,currentBalance,accountNumber,routingNumber);
		if (!bankCache.getCache().containsKey(accountNumber)) {
			bankCache.addToCache(accountNumber, newBank);
			return 1;
		}
		else
			return 0;
	}
	
	/**
	 * Creates a credit obj 
	 * String cardNum, String cardType, int expirationDate, int cVV, double balance)
	 * Stores it to a credit cache if key is unique
	 * Need to update key to be username
	 * @return Credit
	 */
	@Override
	public Credit createCredit(String cardNum, String cardType, int expirationDate, int cVV, double balance) {
		Credit newCredit = new Credit(cardNum, cardType, expirationDate, cVV, balance);
		
		if (!creditCache.getCache().containsKey(cardNum)) {
			creditCache.addToCache(cardNum, newCredit);
			return newCredit;
		}
		else
			return null;
	}

	@Override
	public int createBank(String bankName, double currentBalance, String accountNumber, String routingNumber,
			Customer currentUser) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createCredit(String cardNum, String cardType, int expirationDate, int cVV, double balance,
			Customer currentUser) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
