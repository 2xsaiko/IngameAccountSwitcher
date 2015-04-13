package com.github.mrebhan.ingameaccountswitcher.compat;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;
import com.github.mrebhan.ingameaccountswitcher.tools.Tools;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * @author The_Fireplace
 * Used in the update checker if Fireplace Core is not running alongside In-Game Account Switcher
 */
public class FireCoreCompatAlt implements IFireCoreCompat {

	@Override
	public void sendClientUpdateNotification(EntityPlayer player, String modname, String version, String downloadUrl) {
		if(!Loader.isModLoaded("VersionChecker"))
			sendToPlayer(
					player,
					"A new version of "+modname+" is available!\n=========="
							+ version
							+ "==========\n"
							+ "Download it at " + downloadUrl + " !");
	}
	private static void sendToPlayer(EntityPlayer player, String message) {
		String[] lines = message.split("\n");

		for (String line : lines)
			((ICommandSender) player)
					.addChatMessage(new ChatComponentText(line));
	}
	
	@Override
	public void onPlayerJoinClient(EntityPlayer player,
			ClientConnectedToServerEvent event) {
		if (!IngameAccountSwitcher.prereleaseVersion.equals("")
				&& !IngameAccountSwitcher.releaseVersion.equals("")) {
			if (Tools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.releaseVersion) && Tools.isHigherVersion(IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.releaseVersion)) {
				sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.releaseVersion, IngameAccountSwitcher.downloadURL);
			}else if(Tools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.prereleaseVersion)){
				sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.downloadURL);
			}
		}
	}
}
