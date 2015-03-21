package com.github.mrebhan.minecraft.ingameaccountswitcher;

import java.lang.reflect.Field;

import com.github.mrebhan.minecraft.ingameaccountswitcher.tools.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="IngameAccountSwitcher", name="In-game Account Switcher", version="1.8-r1")
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
}
