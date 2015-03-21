package com.github.mrebhan.minecraft.ingameaccountswitcher.tools.alt;

import java.io.Serializable;

public class AccountData implements Serializable {
	public static final long serialVersionUID = 0xF72DEBAC;
	public String user, pass, alias;
	public AccountData(String user, String pass, String alias) {
		this.user = user;
		this.pass = pass;
		this.alias = alias;
	}
}
