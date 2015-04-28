package com.github.mrebhan.ingameaccountswitcher.tools.alt;

import java.io.Serializable;

import the_fireplace.iasencrypt.EncryptionTools;
/**
 * @author mrebhan
 */
public class AccountData implements Serializable {
	public static final long serialVersionUID = 0xF72DEBAC;
	public String user, pass, alias;

	public AccountData(String user, String pass, String alias) {
		this.user = EncryptionTools.encode(user);
		this.pass = EncryptionTools.encode(pass);
		this.alias = alias;
	}
}
