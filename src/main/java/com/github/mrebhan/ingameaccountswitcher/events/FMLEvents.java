package com.github.mrebhan.ingameaccountswitcher.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import com.github.mrebhan.ingameaccountswitcher.GuiMainMenuIAS;
import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompat;
import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompatAlt;
import com.github.mrebhan.ingameaccountswitcher.compat.IFireCoreCompat;
/**
 * @author mrebhan
 * @author The_Fireplace
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
			@Override
			public void run() {
				while (FMLClientHandler.instance().getClientPlayerEntity() == null)
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}

				onPlayerJoinClient(FMLClientHandler.instance()
						.getClientPlayerEntity(), event);
			}
		}).start();
	}

	public void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event){
		IFireCoreCompat compat;
		if(Loader.isModLoaded("fireplacecore")){
			compat = new FireCoreCompat();
		}else{
			compat = new FireCoreCompatAlt();
		}
		compat.onPlayerJoinClient(player, event);
	}
}
