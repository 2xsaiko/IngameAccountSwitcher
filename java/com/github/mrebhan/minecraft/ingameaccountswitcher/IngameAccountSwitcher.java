package com.github.mrebhan.minecraft.ingameaccountswitcher;

import generic.minecraft.infinityclient.Config;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="IngameAccountSwitcher", name="IngameAccountSwitcher", version="1.8-r0")
public class IngameAccountSwitcher {
	@Instance(value="IngameAccountSwitcher")
	public static IngameAccountSwitcher instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load();
		FMLCommonHandler.instance().bus().register(new Timer());
	}
	
	public static void setSession(Session s) throws Exception {
		Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
		try {
			Field session = mc.getDeclaredField("session");
			session.setAccessible(true);
			session.set(Minecraft.getMinecraft(), s);
			session.setAccessible(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
}
