package com.github.mrebhan.ingameaccountswitcher.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.github.mrebhan.ingameaccountswitcher.tools.Tools;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltDatabase;
import com.github.mrebhan.ingameaccountswitcher.tools.alt.AltManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class GuiAlts extends GuiScreen {
	private int selectedAlt = 0;
	private Throwable failed;
	private ArrayList<AccountData> alts = AltDatabase.getInstance().getAlts();

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.addaccount")));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 154 - 10, this.height - 52, 160, 20, StatCollector.translateToLocal("ias.login")));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 154 - 10, this.height - 28, 110, 20, StatCollector.translateToLocal("ias.login")+" "+StatCollector.translateToLocal("ias.offline")));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 28, 110, 20, StatCollector.translateToLocal("gui.cancel")));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 28, 100, 20, StatCollector.translateToLocal("ias.delete")));
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
		this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.selectaccount"), this.width / 2, 7, -1);
		if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
			this.drawCenteredString(fontRendererObj, StatCollector.translateToLocal("ias.offlinemode"), this.width / 2, this.height - 32, -1);
		}
		if (failed != null) {
			this.drawCenteredString(fontRendererObj, failed.getLocalizedMessage(), this.width / 2, this.height - 22, -1);
		}
		GL11.glPushMatrix();
		GL11.glTranslated(0, 13, 0);
		for (int i = 0; i < alts.size(); i++) {
			this.drawString(fontRendererObj, alts.get(i).alias, 2, i * 12 + 2, -1);
		}
		if(!alts.isEmpty())
			Tools.drawBorderedRect(0, selectedAlt * 12, fontRendererObj.getStringWidth(s) + 4, selectedAlt * 12 + 12, 1, 0xff444444, 0x00000000);
		GL11.glPopMatrix();
		//this.drawCenteredString(fontRendererObj, "[UP,DOWN] to navigate, [ENTER] to login, [SHIFT+ENTER] for offline login, [+] to add, [-] to remove, [ESCAPE] for Main Menu", this.width / 2, this.height - 12, -1);
		super.drawScreen(par1, par2, par3);
	}

	@Override
	protected void actionPerformed(GuiButton button){
		if(button.id == 3){
			mc.displayGuiScreen(null);
		}else if(button.id == 0){
			mc.displayGuiScreen(new GuiAddAlt());
		}else if(button.id == 4 && !alts.isEmpty()){
			AltDatabase.getInstance().getAlts().remove(selectedAlt);
		}else if(button.id == 1 && !alts.isEmpty()){
			AccountData data = alts.get(selectedAlt);
			failed = AltManager.getInstance().setUser(data.user, data.pass);
			if (failed == null) {
				Minecraft.getMinecraft().displayGuiScreen(null);
			}
		}else if(button.id == 2 && !alts.isEmpty()){
			AccountData data = alts.get(selectedAlt);
			AltManager.getInstance().setUserOffline(data.alias);
			failed = null;
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char par1, int par2) {
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
	}
}
