package MoneyAppDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ConnectionUtil.ConnectionUtil;
import MoneyAppPojos.Bank;
import MoneyAppPojos.Customer;


public class BankDaoPostgres implements BankDao {
	
	private PreparedStatement statement;
	private CallableStatement callStatement;
	public ConnectionUtil connUtil = new ConnectionUtil();
	private static Logger log = Logger.getLogger("Web");
	
	public void setConnUtil(ConnectionUtil connUtil) {
		this.connUtil = connUtil;
	}
	
	@Override
	public void createBank(Bank newBank, Customer currentUser) throws SQLException {
		
		//Prepared statement to add a new bank to the database
		String sql = "insert into bank (bank_name,current_balance,account_number,routing_number,username) "
				+ "values(?,?,?,?,?);";

		try (Connection conn = connUtil.createConnection()) {
			statement = conn.prepareStatement(sql);
			statement.setString(1,newBank.getBankName());
			statement.setDouble(2,newBank.getCurrentBalance());
			statement.setString(3, newBank.getAccountNumber());
			statement.setString(4,newBank.getRoutingNumber());
			statement.setString(5,currentUser.getUsername());
			
			statement.executeUpdate();
			
		} catch (SQLException e) {
				log.error("SQLException:" + e);
		}
		
	}

	@Override
	public List<Bank> readBank(Customer currentUser ) throws SQLException {
		
		List<Bank> allBanks = new ArrayList<>();
		
		//Prepared statement to all banks for a given user
		String sql = "select * from bank where username = ?";

		try (Connection conn = connUtil.createConnection()){
				statement = conn.prepareStatement(sql);
				try {
					statement.setString(1, currentUser.getUsername());
				}catch(NullPointerException e) {
					log.error("NPE:" + e);
					return null;
				}
				ResultSet rs = statement.executeQuery();
				
				//Read the result set into something usable
				while(rs.next()) {
					Bank returnedBank = new Bank(rs.getString(1),
											 	 rs.getDouble(2),
											 	 rs.getString(3),
											 	 rs.getString(4));
					allBanks.add(returnedBank);
				}
				
				return allBanks;
				
		}catch (SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
	}

	@Override
	public void updateBank(Bank bankMod,Customer currentUser) throws SQLException {
		
		try (Connection conn = connUtil.createConnection()){ 
			
			String sql = "UPDATE bank SET current_balance = ? WHERE username = ? and account_number = ?;";

            statement = conn.prepareStatement(sql);
            statement.setDouble(1, bankMod.getCurrentBalance());
            statement.setString(2, currentUser.getUsername());
            statement.setString(3, bankMod.getAccountNumber());
            
            statement.executeUpdate();

        } catch (SQLException e) {
        	log.error("SQLException:" + e);
		}
		
	}

	@Override
	public void deleteBank(String username) throws SQLException {
		//Not used in current implementation
		try (Connection conn = connUtil.createConnection()){
			
			String sql = "delete from bank where username = ?;";
			
			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.executeUpdate();
			
		}catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
	}

	@Override
	public boolean RemovedFundsProcedure(String fromCardNum, String toAcctNum, double amount) throws SQLException {

		try (Connection conn = connUtil.createConnection()){ 
			
			String sql = "call remove_money(?,?,?);";
			
			callStatement = conn.prepareCall(sql);
            
			callStatement.setString(1, fromCardNum);
			callStatement.setString(2, toAcctNum);
			callStatement.setDouble(3, amount);
            
			callStatement.execute();
             return true;

        } catch (SQLException e) {
        	log.error("SQLException:" + e);;
        	return false;
		}
		
	}

}
