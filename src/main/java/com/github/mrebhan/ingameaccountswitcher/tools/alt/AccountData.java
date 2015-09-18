package com.github.mrebhan.ingameaccountswitcher.tools.alt;

import java.io.Serializable;

import the_fireplace.iasencrypt.EncryptionTools;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class AccountData implements Serializable {
	public static final long serialVersionUID = 0xF72DEBAC;
	public String user, pass, alias;

	public AccountData(String user, String pass, String alias) {
		this.user = EncryptionTools.encode(user);
		this.pass = EncryptionTools.encode(pass);
		this.alias = alias;
	}

	public boolean equalsBasic(Object obj){
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AccountData other = (AccountData) obj;
		if(!user.equals(other.user)){
			return false;
		}
		return true;
	}
}
