package com.github.mrebhan.ingameaccountswitcher.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import the_fireplace.fireplacecore.FireCoreBaseFile;
import the_fireplace.fireplacecore.api.FCAPI;

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
		return;
	}
	@Optional.Method(modid = "fireplacecore")
	@Override
	public void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event) {
		return;
	}
	@Optional.Method(modid = "fireplacecore")
	@Override
	public void registerUpdate(NBTTagCompound updateInfo, String modDisplayName, String oldVersion, String newPreVersion, String newVersion, String updateURL, String modid) {
		FCAPI.registerModToVersionChecker(updateInfo, modDisplayName, oldVersion, newPreVersion, newVersion, updateURL, modid);
	}
}
