package MoneyAppMain;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import MoneyAppController.WebController;
import MoneyAppDao.BankDao;
import MoneyAppDao.BankDaoPostgres;
import MoneyAppDao.UserDao;
import MoneyAppDao.UserDaoPostgres;
import MoneyAppServices.UserSignIn;
import MoneyAppWebServices.FrontWebImpl;
import io.javalin.Javalin;

public interface ServerDriver {
	
	public static UserSignIn userController = new FrontWebImpl();
	public static UserDao userDao = new UserDaoPostgres();
	public static BankDao bankDao = new BankDaoPostgres();
	public static FrontWebImpl test = new FrontWebImpl();
	public static WebController web = new WebController();
	public static Logger log = Logger.getLogger("Driver");
	
	public static void main(String[] args) {
		Javalin app = Javalin.create().start(9090); //sets up and starts our server
		
		log.info("Server has started");
		log.info("Program has started");
		
		//
		app.get("/welcome", ctx -> ctx.html("Welcome to Money App"));
		app.get("/customer", ctx -> web.callSignIn(ctx));
		app.post("/customer", ctx -> web.callCreateUser(ctx));
		app.post("/banks", ctx -> web.callCreateBank(ctx));
		app.post("/cards", ctx -> web.callCreateCredit(ctx));
		app.put("/transfer-service/send", ctx -> web.callSendMoney(ctx));
		app.put("/transfer-service/withdraw", ctx -> web.callAddFunds(ctx));
		app.put("/transfer-service/deposit", ctx -> web.callRemoveFunds(ctx));
		app.get("/bank-balances", ctx -> web.CallAllBanks(ctx));
		app.get("/card-balances", ctx -> web.CallAllCards(ctx));
		app.delete("/account-management/customer", ctx -> web.CallDeleteCustomer(ctx));
		app.delete("/account-management/banks", ctx -> web.CallDeleteBank(ctx));
		app.delete("/account-management/cards", ctx -> web.CallDeleteCard(ctx));
		
		
		
		
		log.info("Program has ended");
	}

}
