package com.github.mrebhan.ingameaccountswitcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompat;
import com.github.mrebhan.ingameaccountswitcher.compat.FireCoreCompatAlt;
import com.github.mrebhan.ingameaccountswitcher.compat.IFireCoreCompat;
import com.github.mrebhan.ingameaccountswitcher.events.FMLEvents;
import com.github.mrebhan.ingameaccountswitcher.events.ForgeEvents;
import com.github.mrebhan.ingameaccountswitcher.tools.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import the_fireplace.iasencrypt.Standards;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
@Mod(modid=IngameAccountSwitcher.MODID, name=IngameAccountSwitcher.MODNAME, version=IngameAccountSwitcher.VERSION, clientSideOnly=true)
public class IngameAccountSwitcher {
	@Instance(value=IngameAccountSwitcher.MODID)
	public static IngameAccountSwitcher instance;
	public static final String MODID = "IngameAccountSwitcher";
	public static final String MODNAME = "In-game Account Switcher";
	public static final String VERSION = "2.1.0.3";
	public static String releaseVersion = "";
	public static String prereleaseVersion = "";
	public static final String downloadURL = "http://goo.gl/1erpBM";
	//For Dynious's Version Checker
	public static NBTTagCompound update = new NBTTagCompound();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Standards.generateConfigFile();
		Config.load();
		FMLCommonHandler.instance().bus().register(new FMLEvents());
		MinecraftForge.EVENT_BUS.register(new ForgeEvents());
		retriveCurrentVersions();
		IFireCoreCompat compat;
		if(Loader.isModLoaded("fireplacecore"))
			compat = new FireCoreCompat();
		else
			compat = new FireCoreCompatAlt();
		compat.registerUpdate(update, this.MODNAME, this.VERSION, this.prereleaseVersion, this.releaseVersion, this.downloadURL, this.MODID);
	}

	public static void setSession(Session s) throws Exception {
		Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
		try {
			Field session = null;

			for (Field f : mc.getDeclaredFields()) {
				if (f.getType().isInstance(s)) {
					session = f;
					System.out.println("Found field " + f.toString() + ", injecting...");
				}
			}

			if (session == null) {
				throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
			}

			session.setAccessible(true);
			session.set(Minecraft.getMinecraft(), s);
			session.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Retrieves what the latest version is from Dropbox
	 */
	private static void retriveCurrentVersions() {
		try {
			releaseVersion = get_content(new URL(
					"https://dl.dropboxusercontent.com/s/l2i7ua5u4j5i8sc/release.version?dl=0")
					.openConnection());

			prereleaseVersion = get_content(new URL(
					"https://dl.dropboxusercontent.com/s/55rwhwvai453yqz/prerelease.version?dl=0")
					.openConnection());

		} catch (final MalformedURLException e) {
			releaseVersion = "";
			prereleaseVersion = "";
		} catch (final IOException e) {
			releaseVersion = "";
			prereleaseVersion = "";
		}
	}

	private static String get_content(URLConnection con) throws IOException {
		String output = "";

		if (con != null) {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));

			String input;

			while ((input = br.readLine()) != null) {
				output = output + input;
			}
			br.close();
		}

		return output;
	}
}
