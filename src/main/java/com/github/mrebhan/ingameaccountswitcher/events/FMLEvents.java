package com.github.mrebhan.ingameaccountswitcher.events;

import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompat;
import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompatAlt;
import com.github.mrebhan.ingameaccountswitcher.compat.IFireCoreCompat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class FMLEvents {

	@SubscribeEvent
	public void onTick(RenderTickEvent t) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen instanceof GuiMainMenu) {
			screen.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, StatCollector.translateToLocal("ias.loggedinas") + Minecraft.getMinecraft().getSession().getUsername()+".", screen.width / 2, screen.height / 4 + 48 + 72 + 12 + 22, 0xFFCC8888);
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
