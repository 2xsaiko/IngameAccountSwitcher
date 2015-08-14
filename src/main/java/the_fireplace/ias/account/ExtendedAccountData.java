package the_fireplace.ias.account;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;

import the_fireplace.ias.enums.EnumBool;

public class ExtendedAccountData extends AccountData {

	public EnumBool premium;
	public String[] lastused;
	public int useCount;

	public ExtendedAccountData(String user, String pass, String alias) {
		super(user, pass, alias);
		useCount = 0;
		lastused = new String[3];
		premium = EnumBool.UNKNOWN;
	}

}
