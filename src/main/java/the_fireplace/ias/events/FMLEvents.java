package the_fireplace.ias.events;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
/**
 * @author The_Fireplace
 */
public class FMLEvents {

	@SubscribeEvent
	public void onTick(RenderTickEvent t) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if (screen instanceof GuiMainMenu) {
			screen.drawCenteredString(Minecraft.getMinecraft().fontRenderer, StatCollector.translateToLocal("ias.loggedinas") + Minecraft.getMinecraft().getSession().getUsername()+".", screen.width / 2, screen.height / 4 + 48 + 72 + 12 + 22, 0xFFCC8888);
		}else if(screen instanceof GuiMultiplayer){
			if (Minecraft.getMinecraft().getSession().getToken().equals("0")) {
				screen.drawCenteredString(Minecraft.getMinecraft().fontRenderer, StatCollector.translateToLocal("ias.offlinemode"), screen.width / 2, 10, 16737380);
			}
		}
	}
	@SubscribeEvent
	public void configChanged(ConfigChangedEvent event){
		if(event.modID.equals(IngameAccountSwitcher.MODID)){
			IngameAccountSwitcher.syncConfig();
		}
	}
}
