package the_fireplace.ias.tools;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * Used in the update checker. Modified code from the FULCRUM Update Checker.
 * @author The_Fireplace
 */
public class VersionChecker {

	public static void sendClientUpdateNotification(EntityPlayer player, String modname, String version, String downloadUrl) {
		if(!Loader.isModLoaded("VersionChecker")){
			ICommandSender ics = player;
			ics.addChatMessage(new ChatComponentText("A new version of "+modname+" is available!"));
			ics.addChatMessage(new ChatComponentText("=========="+version+"=========="));
			ics.addChatMessage(new ChatComponentText("Get it at the following link:"));
			ics.addChatMessage(new ChatComponentText(downloadUrl).setChatStyle(new ChatStyle().setItalic(true).setUnderlined(true).setColor(EnumChatFormatting.BLUE).setChatClickEvent(new ClickEvent(Action.OPEN_URL, downloadUrl))));
		}
	}

	public static void onPlayerJoinClient(EntityPlayer player,
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
	public static void registerUpdate(NBTTagCompound updateInfo,
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
