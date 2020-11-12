  
package MoneyAppServices;

public interface UserSignIn {
	
	//Create User if retrieve User is false
	public boolean createUser(String username, String password, String email, String phoneNum, String firstName, String lastName);	
	
	//Sign in user if password is successful
	public int signIn(String username, String password);

}
