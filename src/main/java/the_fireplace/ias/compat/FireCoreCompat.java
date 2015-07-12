package the_fireplace.ias.compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import the_fireplace.fireplacecore.api.FCAPI;
/**
 * Used in the update checker if Fireplace Core is running alongside In-Game Account Switcher.
 * Allows the use of the same update notification format as other mods using Fireplace Core.
 * Also allows you to disable the update checker via the Fireplace Core config.
 *
 * @author The_Fireplace
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
