package com.github.mrebhan.minecraft.ingameaccountswitcher;

import generic.minecraft.infinityclient.Config;
import generic.minecraft.infinityclient.alt.AltDatabase;
import generic.minecraft.infinityclient.ui.GuiAlts;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;

public class GuiMainMenuIAS extends GuiMainMenu {

	@Override
	public void initGui() {
		Config.save();
		super.initGui();
		int i = this.height / 4 + 48;
		this.buttonList.add(new GuiButtonWithImage(20, this.width / 2 + 104, i + 72 + 12, 20, 20, ""));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(fontRendererObj, "You are currently logged in as " + Minecraft.getMinecraft().getSession().getUsername(), this.width / 2, this.height / 4 + 48 + 72 + 12 + 22, 0xFFCC8888);
	}
	
	@Override
    protected void actionPerformed(GuiButton button) throws IOException {
    	if (button.id == 20) {
    		Minecraft.getMinecraft().displayGuiScreen(new GuiAlts());
    	} else {
    		super.actionPerformed(button);
    	}
    }
	
}
