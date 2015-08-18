package com.github.mrebhan.ingameaccountswitcher.tools.alt;

import java.util.UUID;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;
import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import the_fireplace.ias.account.AlreadyLoggedInException;
import the_fireplace.iasencrypt.EncryptionTools;
/**
 * @author mrebhan
 */
public class AltManager {
	private static AltManager manager = null;
	public AuthenticationService authService;
	public MinecraftSessionService sessionService;
	public UUID uuid;
	public UserAuthentication auth;
	private String currentUser;
	private String currentPass;

	private AltManager() {
		uuid = UUID.randomUUID();
		authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
		auth = authService.createUserAuthentication(Agent.MINECRAFT);
		sessionService = authService.createMinecraftSessionService();
	}

	public static AltManager getInstance() {
		if (manager == null) {
			manager = new AltManager();
		}

		return manager;
	}

	public Throwable setUser(String username, String password) {
		Throwable throwable = null;
		if(Minecraft.getMinecraft().getSession().getUsername() != EncryptionTools.decode(username)){
			for (AccountData data : AltDatabase.getInstance().getAlts()) {
				if (data.alias.equals(Minecraft.getMinecraft().getSession().getUsername()) && data.user.equals(username)) {
					throwable = new AlreadyLoggedInException();
					return throwable;
				}
			}
			this.auth.logOut();
			this.auth.setUsername(EncryptionTools.decode(username));
			this.auth.setPassword(EncryptionTools.decode(password));
			try {
				this.auth.logIn();
				Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
				IngameAccountSwitcher.setSession(session);
				for (int i = 0; i < AltDatabase.getInstance().getAlts().size(); i++) {
					AccountData data = AltDatabase.getInstance().getAlts().get(i);
					if (data.user.equals(username) && data.pass.equals(password)) {
						data.alias = session.getUsername();
					}
				}
			} catch (Exception e) {
				throwable = e;
			}
		}else{
			throwable = new AlreadyLoggedInException();
		}
		return throwable;
	}

	public boolean setUserOffline(String username) {
		this.auth.logOut();
		Session session = new Session(username, username, "0", "legacy");
		try {
			IngameAccountSwitcher.setSession(session);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
