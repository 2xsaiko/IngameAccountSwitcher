package com.github.mrebhan.ingameaccountswitcher.gui;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class GuiAddAlt extends GuiScreen {

	private String user = "", pass = "";
	private int loc = 0;

	@Override
	public void initGui() {

	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if (loc == 2) {
			AltDatabase.getInstance().getAlts().add(new AccountData(user, pass, user));
			mc.displayGuiScreen(new GuiAlts());
		}
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, "Add Alt", this.width / 2, 7, -1);
		this.drawString(fontRendererObj, "Username: " + user + (loc == 0 ? "_" : ""), 1, 16, -1);
		String var1 = "";
		for (int i = 0; i < pass.length(); i++) {
			var1 += "*";
		}
		this.drawString(fontRendererObj, "Password: " + var1 + (loc == 1 ? "_" : ""), 1, 16 + 9, -1);
		this.drawCenteredString(fontRendererObj, "§7[ENTER]§r for next, §7[ESCAPE]§r for backward", this.width / 2, this.height - 12, -1);
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == Keyboard.KEY_ESCAPE) {
			if (loc == 0) {
				mc.displayGuiScreen(new GuiAlts());
			} else if (loc == 1) {
				loc = 0;
				pass = "";
			}
		} else if (par2 == Keyboard.KEY_RETURN) {
			loc++;
		} else if (par2 == Keyboard.KEY_BACK) {
			try {
				if (loc == 0) {
					user = user.substring(0, user.length() - 1);
				} else if (loc == 1) {
					pass = pass.substring(0, pass.length() - 1);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (par1 != 0) {
			if (loc == 0) {
				user += par1;
			} else if (loc == 1) {
				pass += par1;
			}
		}
	}
}
