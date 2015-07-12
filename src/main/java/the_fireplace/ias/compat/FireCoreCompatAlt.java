package the_fireplace.ias.compat;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import the_fireplace.ias.tools.VersionSystemTools;
/**
 * Used in the update checker if Fireplace Core is not running alongside In-Game Account Switcher.
 * @author The_Fireplace
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
			if (VersionSystemTools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.releaseVersion) && VersionSystemTools.isHigherVersion(IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.releaseVersion)) {
				sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.releaseVersion, IngameAccountSwitcher.downloadURL);
			}else if(VersionSystemTools.isHigherVersion(IngameAccountSwitcher.VERSION, IngameAccountSwitcher.prereleaseVersion)){
				sendClientUpdateNotification(player, IngameAccountSwitcher.MODNAME, IngameAccountSwitcher.prereleaseVersion, IngameAccountSwitcher.downloadURL);
			}
		}
	}
	@Override
	public void registerUpdate(NBTTagCompound updateInfo,
			String modDisplayName, String oldVersion, String newPreVersion,
			String newVersion, String updateURL, String modid) {
		String versiontoshow;
		if (!newVersion.equals("") && !newPreVersion.equals("")) {//Prevents crashing if the connection to the server failed.
			if(VersionSystemTools.isHigherVersion(newVersion, newPreVersion)){
				versiontoshow = newPreVersion;
			}else{
				versiontoshow = newVersion;
			}
		}else{
			versiontoshow = "0.0.0.0";
		}
		updateInfo.setString("modDisplayName", modDisplayName);
		updateInfo.setString("oldVersion", oldVersion);
		updateInfo.setString("newVersion", versiontoshow);
		updateInfo.setString("updateURL", updateURL);
		updateInfo.setBoolean("isDirectLink", false);
		if(VersionSystemTools.isHigherVersion(oldVersion, versiontoshow))
			FMLInterModComms.sendRuntimeMessage(modid, "VersionChecker", "addUpdate", updateInfo);
	}
}
