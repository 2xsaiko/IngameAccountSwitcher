package com.github.mrebhan.ingameaccountswitcher.events;

import com.github.mrebhan.ingameaccountswitcher.GuiMainMenuIAS;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * @author mrebhan
 * @author The_Fireplace
 * This class was formerly named Timer
 */
public class FMLEvents {
	
	@SubscribeEvent
	public void onTick(RenderTickEvent t) {
		if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu &!(Minecraft.getMinecraft().currentScreen instanceof GuiMainMenuIAS)) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenuIAS());
		}
	}
	
	@SubscribeEvent
	public void onPlayerJoinClient(final ClientConnectedToServerEvent event) {
		(new Thread() {
			public void run() {
				while (FMLClientHandler.instance().getClientPlayerEntity() == null)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

				onPlayerJoinClientt(FMLClientHandler.instance()
						.getClientPlayerEntity(), event);
			}
		}).start();
	}
	
	public void onPlayerJoinClientt(EntityPlayer player, ClientConnectedToServerEvent event){
		
	}
}
