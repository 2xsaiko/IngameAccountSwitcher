package com.github.mrebhan.ingameaccountswitcher.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import the_fireplace.fireplacecore.FireCoreBaseFile;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;
import com.github.mrebhan.ingameaccountswitcher.tools.Tools;
/**
 * @author The_Fireplace
 * Used in the update checker if Fireplace Core is running alongside In-Game Account Switcher.
 * Allows the use of the same update notification format as other mods using Fireplace Core.
 * Also allows you to disable the update checker via the Fireplace Core config.
 */
public class FireCoreCompat implements IFireCoreCompat {
	@Optional.Method(modid = "fireplacecore")
	@Override
	public void sendClientUpdateNotification(EntityPlayer player, String modname, String version, String downloadUrl) {
		the_fireplace.fireplacecore.FireCoreBaseFile.sendClientUpdateNotification(player, modname, version, downloadUrl);
	}
	@Optional.Method(modid = "fireplacecore")
	@Override
	public void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event) {
		if (!IngameAccountSwitcher.prereleaseVersion.equals("")
				&& !IngameAccountSwitcher.releaseVersion.equals("")) {
			switch (FireCoreBaseFile.instance.getUpdateNotification()) {
			case 0:
				if (Tools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.releaseVersion) && Tools.isHigherVersion(IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.releaseVersion)) {
					sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.releaseVersion, IngameAccountSwitcher.downloadURL);
				}else if(Tools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.prereleaseVersion)){
					sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.downloadURL);
				}

				break;
			case 1:
				if (Tools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.releaseVersion)) {
					sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.releaseVersion, IngameAccountSwitcher.downloadURL);
				}
				break;
			case 2:

				break;
			}
		}
	}

}
