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
import MoneyAppPojos.Credit;
import MoneyAppPojos.Customer;

public class CreditDaoPostgres implements CreditDao {
	
	private PreparedStatement statement;
	private CallableStatement callStatement;
	private static Logger log = Logger.getLogger("Web");
	public ConnectionUtil connUtil = new ConnectionUtil();
	
	public void setConnUtil(ConnectionUtil connUtil) {
		this.connUtil = connUtil;
	}
	
	@Override
	public void createCredit(Credit newCard, Customer currentUser) throws SQLException {
		
		String sql = "insert into credit (card_num,card_type,exp_date,cvv,balance,username) "
				+ "values(?,?,?,?,?,?);";

		try (Connection conn = connUtil.createConnection()){
				
			statement = conn.prepareStatement(sql);
				statement.setString(1,newCard.getCardNum());
				statement.setString(2,newCard.getCardType());
				statement.setInt(3, newCard.getExpirationDate());
				statement.setInt(4,newCard.getCVV());
				statement.setDouble(5,newCard.getBalance());
				statement.setString(6,currentUser.getUsername());
			
				statement.executeUpdate();
				
		} catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
	}

	@Override
	public List<Credit> readCredit(Customer currentUser) throws SQLException {
		
		String sql = "select * from credit where username = ?;";
		List<Credit> allCards = new ArrayList<>();
		
		try (Connection conn = connUtil.createConnection()){
				
				statement = conn.prepareStatement(sql);
				
				try {
					statement.setString(1, currentUser.getUsername());
				}catch(NullPointerException e){
					//log.error no such username
					return null;
				}
				ResultSet rs = statement.executeQuery();
				
				while(rs.next()) {
					Credit returnedCredit = new Credit(rs.getString(1),
													   rs.getString(2),
													   rs.getInt(3),
													   rs.getInt(4),
													   rs.getDouble(5));
					allCards.add(returnedCredit);
				}
				return allCards;
				
		}catch (SQLException e) {
			log.error("SQLException:" + e);
			return null;
		}
	}

	@Override
	public void updateCredit(Credit cardMod, Customer currentUser) throws SQLException {
		
		try (Connection conn = connUtil.createConnection()){ 
			String sql = "UPDATE credit SET balance = ? WHERE username = ? and card_num = ?;";
			
            statement = conn.prepareStatement(sql);
            
            statement.setDouble(1, cardMod.getBalance());
            statement.setString(2, currentUser.getUsername());
            statement.setString(3, cardMod.getCardNum());
            
            statement.executeUpdate();

        } catch (SQLException e) {
        	log.error("SQLException:" + e);
		}
		
	}

	@Override
	public void deleteCredit(String username) throws SQLException {
		
		String sql = "delete from credit where username = ?;";
		
		try (Connection conn = connUtil.createConnection()){
			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.executeUpdate();
		}catch (SQLException e) {
			log.error("SQLException:" + e);
		}
		
	}

	@Override
	public boolean SendMoneyProcedure(String fromCardNum, String toCardNum, double amount) throws SQLException {
		
		try (Connection conn = connUtil.createConnection()){ 
			String sql = "call send_money(?,?,?);";
			
            callStatement = conn.prepareCall(sql);
            
            callStatement.setString(1, fromCardNum);
            callStatement.setString(2, toCardNum);
            callStatement.setDouble(3, amount);
            
            callStatement.execute(); 
            
            return true;

        } catch (SQLException e) {
        	log.error("SQLException:" + e);
        	return false;
		}	
	}

	@Override
	public boolean AddFundsProcedure(String fromAcctNum, String toCardNum, double amount) throws SQLException {

		try (Connection conn = connUtil.createConnection()){ 
			String sql = "call add_money(?,?,?);";
			
            callStatement = conn.prepareCall(sql);
            
            callStatement.setString(1, fromAcctNum);
            callStatement.setString(2, toCardNum);
            callStatement.setDouble(3, amount);
            
            callStatement.execute();
            return true;

        } catch (SQLException e) {
        	log.error("SQLException:" + e);
        	return true;
		}
	}

}
