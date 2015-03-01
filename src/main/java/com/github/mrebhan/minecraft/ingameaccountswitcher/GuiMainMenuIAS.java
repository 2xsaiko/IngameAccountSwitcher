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
		String localString;
		if(languageIs("de")){
			localString = "Sie sind derzeit angemeldat als ";
		}else if(languageIs("es")){
			localString = "En este momento est\u00E1 conectado como ";//á is \u00E1
		}else if(languageIs("fr")){
			localString = "Vous \u00EAtes actuellement connect\u00E9 en tant ";//ê is \u00EA   é is \u00E9
		}else{
			localString = "You are currently logged in as ";
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(fontRendererObj, localString + Minecraft.getMinecraft().getSession().getUsername(), this.width / 2, this.height / 4 + 48 + 72 + 12 + 22, 0xFFCC8888);
	}
	
	private boolean languageIs(String languagecode){
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode().contains(languagecode+"_");
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
