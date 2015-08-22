package com.github.mrebhan.ingameaccountswitcher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;

import com.github.mrebhan.ingameaccountswitcher.tools.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import the_fireplace.ias.config.ConfigValues;
import the_fireplace.ias.events.FMLEvents;
import the_fireplace.ias.events.ForgeEvents;
import the_fireplace.ias.tools.SkinTools;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
@Mod(modid=IngameAccountSwitcher.MODID, name=IngameAccountSwitcher.MODNAME, version=IngameAccountSwitcher.VERSION, clientSideOnly=true, guiFactory="the_fireplace.ias.config.IASGuiFactory")
public class IngameAccountSwitcher {
	@Instance(value=IngameAccountSwitcher.MODID)
	public static IngameAccountSwitcher instance;
	public static final String MODID = "IngameAccountSwitcher";
	public static final String MODNAME = "In-game Account Switcher";
	public static final String VERSION = "2.2.2.0";
	public static final String downloadURL = "http://goo.gl/1erpBM";
	public static Configuration config;

	public static Property CASESENSITIVE_PROPERTY;

	public static void syncConfig(){
		ConfigValues.CASESENSITIVE = CASESENSITIVE_PROPERTY.getBoolean();
		if(config.hasChanged()){
			config.save();
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		CASESENSITIVE_PROPERTY = config.get(Configuration.CATEGORY_GENERAL, ConfigValues.CASESENSITIVE_NAME, ConfigValues.CASESENSITIVE_DEFAULT, ConfigValues.CASESENSITIVE_NAME+".tooltip");
		syncConfig();
		Config.load();
		FMLCommonHandler.instance().bus().register(new FMLEvents());
		MinecraftForge.EVENT_BUS.register(new ForgeEvents());
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		try {
			Files.createDirectory(SkinTools.cachedir.toPath());
		} catch (IOException e) {}
		SkinTools.cacheSkins();
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
	public static final String LATEST = "https://dl.dropboxusercontent.com/s/l2i7ua5u4j5i8sc/release.version?dl=0";
}
