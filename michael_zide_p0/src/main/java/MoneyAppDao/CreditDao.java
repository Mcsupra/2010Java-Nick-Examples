package MoneyAppDao;

import java.sql.SQLException;
import java.util.List;

import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;

public interface CreditDao {
	
	public void createCredit(Credit newCard, Customer currentUser) throws SQLException;

	public List<Credit> readCredit(Customer currentUser) throws SQLException;

	public void updateCredit(Credit cardMod,Customer currentUser) throws SQLException;

	public void deleteCredit(String username) throws SQLException;
	
	public boolean SendMoneyProcedure(String fromCardNum, String toCardNum, double amount) throws SQLException;
	
	public boolean AddFundsProcedure(String fromAcctNum, String toCardNum, double amount) throws SQLException;
}
