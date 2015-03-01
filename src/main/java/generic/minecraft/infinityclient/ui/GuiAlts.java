package generic.minecraft.infinityclient.ui;

import generic.minecraft.infinityclient.Tools;
import generic.minecraft.infinityclient.alt.AccountData;
import generic.minecraft.infinityclient.alt.AltDatabase;
import generic.minecraft.infinityclient.alt.AltManager;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiAlts extends GuiScreen {
	private int selectedAlt = 0;
	private Throwable failed;
	private ArrayList<AccountData> alts = AltDatabase.getInstance().getAlts();

	@Override
	public void initGui() {

	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		String s = "";
		try {
			s = alts.get(selectedAlt).alias;
		} catch (Exception e) {
			if (selectedAlt > 0) {
				selectedAlt--;
			}
		}
		this.drawDefaultBackground();
		this.drawCenteredString(fontRendererObj, "Select an account", this.width / 2, 7, -1);
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			//this.drawCenteredString(fontRendererObj, "§cYou are currently in offline mode. You won't be able to join online servers.", this.width / 2, this.height - 32, -1);
			this.drawCenteredString(fontRendererObj, "\u00A7cYou are currently in offline mode. You won't be able to join online servers.", this.width / 2, this.height - 32, -1);
		}
		if (failed != null) {
			//this.drawCenteredString(fontRendererObj, "§c" + failed.getLocalizedMessage(), this.width / 2, this.height - 22, -1);
			this.drawCenteredString(fontRendererObj, "\u00A7c" + failed.getLocalizedMessage(), this.width / 2, this.height - 22, -1);
		}
		GL11.glPushMatrix();
		GL11.glTranslated(0, 13, 0);
		for (int i = 0; i < alts.size(); i++) {
			this.drawString(fontRendererObj, alts.get(i).alias, 2, i * 12 + 2, -1);
		}
		Tools.drawBorderedRect(0, selectedAlt * 12, fontRendererObj.getStringWidth(s) + 4, selectedAlt * 12 + 12, 1, 0xff444444, 0x00000000);
		GL11.glPopMatrix();
		//this.drawCenteredString(fontRendererObj, "§7[UP,DOWN]§r to navigate, §7[ENTER]§r to login, §7[SHIFT+ENTER]§r for offline login, §7[+]§r to add, §7[-]§r to remove, §7[ESCAPE]§r for Main Menu", this.width / 2, this.height - 12, -1);
		//The � shows up in the game, I am not sure why it is there. Leaving old code line for convenience in case there is a reason for it.
		this.drawCenteredString(fontRendererObj, "\u00A77[UP,DOWN]\u00A7r to navigate, \u00A77[ENTER]\u00A7r to login, \u00A77[SHIFT+ENTER]\u00A7r for offline login, \u00A77[+]\u00A7r to add, \u00A77[-]\u00A7r to remove, \u00A77[ESCAPE]\u00A7r for Main Menu", this.width / 2, this.height - 12, -1);
		//Unicode for the Section symbol(�) is \u00A7
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
		}
		if (par1 == '+') {
			mc.displayGuiScreen(new GuiAddAlt());
		}
		if (par1 == '-') {
			try {
				AltDatabase.getInstance().getAlts().remove(selectedAlt);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (par2 == Keyboard.KEY_UP) {
			if (selectedAlt > 0) {
				selectedAlt--;
			}
		}
		if (par2 == Keyboard.KEY_DOWN) {
			if (selectedAlt < alts.size() - 1) {
				selectedAlt++;
			}
		}
		if (par2 == Keyboard.KEY_RETURN) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				AccountData data = alts.get(selectedAlt);
				AltManager.getInstance().setUserOffline(data.alias);
				failed = null;
				Minecraft.getMinecraft().displayGuiScreen(null);
				return;
			}
			try {
				AccountData data = alts.get(selectedAlt);
				failed = AltManager.getInstance().setUser(data.user, data.pass);
				if (failed == null) {
					Minecraft.getMinecraft().displayGuiScreen(null);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
