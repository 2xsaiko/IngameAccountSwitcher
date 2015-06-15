package com.github.mrebhan.ingameaccountswitcher.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * @author The_Fireplace
 * Used in the update checker to use Fireplace Core's update formatting and settings if available.
 */
public interface IFireCoreCompat {
	public void sendClientUpdateNotification(EntityPlayer player, String modname, String version, String downloadUrl);
	public void onPlayerJoinClient(EntityPlayer player, ClientConnectedToServerEvent event);
	public void registerUpdate(NBTTagCompound updateInfo, String modDisplayName, String oldVersion, String newPreVersion, String newVersion, String updateURL, String modid);
}
