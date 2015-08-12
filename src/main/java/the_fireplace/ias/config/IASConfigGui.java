package the_fireplace.ias.config;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class IASConfigGui extends GuiConfig {

	public IASConfigGui(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(IngameAccountSwitcher.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), IngameAccountSwitcher.MODID, false, false, GuiConfig.getAbridgedConfigPath(IngameAccountSwitcher.config.toString()));
	}

}
