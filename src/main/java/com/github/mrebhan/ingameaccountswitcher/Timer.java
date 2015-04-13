package com.github.mrebhan.ingameaccountswitcher;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Timer {
	
	@SubscribeEvent
	public void onTick(RenderTickEvent t) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu &!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenuIAS)) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenuIAS());
		}
	}
}
