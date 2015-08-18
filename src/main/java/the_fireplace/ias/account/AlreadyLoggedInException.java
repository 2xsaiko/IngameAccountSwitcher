package the_fireplace.ias.account;

public class AlreadyLoggedInException extends Exception {
	@Override
	public String getLocalizedMessage(){
		return "You are already logged in to this account";
	}
}
