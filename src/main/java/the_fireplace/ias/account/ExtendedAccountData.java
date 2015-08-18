package the_fireplace.ias.account;

import java.util.Arrays;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;

import the_fireplace.ias.enums.EnumBool;
import the_fireplace.ias.tools.JavaTools;

public class ExtendedAccountData extends AccountData {

	public EnumBool premium;
	public int[] lastused;
	public int useCount;

	public ExtendedAccountData(String user, String pass, String alias) {
		super(user, pass, alias);
		useCount = 0;
		lastused = JavaTools.getJavaCompat().getDate();
		premium = EnumBool.UNKNOWN;
	}

	public ExtendedAccountData(String user, String pass, String alias, int useCount, int[] lastused, EnumBool premium) {
		super(user, pass, alias);
		this.useCount = useCount;
		this.lastused = lastused;
		this.premium = premium;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ExtendedAccountData other = (ExtendedAccountData) obj;
		if (!Arrays.equals(lastused, other.lastused)) {
			return false;
		}
		if (premium != other.premium) {
			return false;
		}
		if (useCount != other.useCount) {
			return false;
		}
		if(!user.equals(other.user)){
			return false;
		}
		if(!pass.equals(other.pass)){
			return false;
		}
		return true;
	}
}
