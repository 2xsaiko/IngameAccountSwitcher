package the_fireplace.ias.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import com.github.mrebhan.ingameaccountswitcher.IngameAccountSwitcher;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
/**
 * This is the embeddable version checker I made. Designed for convenience, all you have to do is paste this class, change the HostMODNAME, HostMODID, HostVERSION, and how it retrieves the latest version and link to the download page.
 * @author The_Fireplace
 */
@Mod(modid=VersionChecker.MODID, name=VersionChecker.MODNAME, version=VersionChecker.VERSION)
public class VersionChecker {
	private static final String HostMODID=IngameAccountSwitcher.MODID;
	private static final String HostMODNAME=IngameAccountSwitcher.MODNAME;
	private static final String HostVERSION=IngameAccountSwitcher.VERSION;
	static final String MODID=IngameAccountSwitcher.MODID+"vc";
	static final String MODNAME=IngameAccountSwitcher.MODNAME+" Version Checker";
	static final String VERSION="1.0";
	private String latest;
	private String downloadURL;

	public VersionChecker(){
		latest = stringAt(IngameAccountSwitcher.LATEST);
		downloadURL = IngameAccountSwitcher.downloadURL;
	}

	private void tryNotifyClient(EntityPlayer player){
		if(!Loader.isModLoaded("VersionChecker") && isHigherVersion()){
			ICommandSender ics = player;
			ics.addChatMessage(new ChatComponentText("A new version of "+HostMODNAME+" is available!"));
			ics.addChatMessage(new ChatComponentText("=========="+latest+"=========="));
			ics.addChatMessage(new ChatComponentText("Get it at the following link:"));
			ics.addChatMessage(new ChatComponentText(downloadURL).setChatStyle(new ChatStyle().setItalic(true).setUnderlined(true).setColor(EnumChatFormatting.BLUE).setChatClickEvent(new ClickEvent(Action.OPEN_URL, downloadURL))));
		}
	}

	private void notifyServer(){
		System.out.println("Version "+latest+" of "+HostMODNAME+" is available!");
		System.out.println("Download it at "+downloadURL);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		event.getModMetadata().modId=MODID;
		event.getModMetadata().name=MODNAME;
		event.getModMetadata().version=VERSION;
		event.getModMetadata().credits="The_Fireplace(Making the embeddable Version Checker)";
		event.getModMetadata().authorList=Arrays.asList(new String[]{"The_Fireplace"});
		event.getModMetadata().parent=HostMODID;
	}
	@EventHandler
	public void init(FMLInitializationEvent event){
		tryNotifyDynious();
		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event){
		tryNotifyServer();
	}

	@SubscribeEvent
	public void onPlayerJoinClient(final ClientConnectedToServerEvent event){
		(new Thread(){
			@Override
			public void run(){
				while(FMLClientHandler.instance().getClientPlayerEntity() == null)
					try{
						Thread.sleep(100);
					}catch(InterruptedException e){

					}
				tryNotifyClient(FMLClientHandler.instance().getClientPlayerEntity());
			}
		}).start();
	}

	private boolean isHigherVersion(){
		final int[] _current = splitVersion(HostVERSION);
		final int[] _new = splitVersion(latest);

		for(int i=0;i<Math.max(_current.length, _new.length);i++){
			int curv=0;
			if(i < _current.length)
				curv = _current[i];
			int newv=0;
			if(i<_new.length)
				newv=_new[i];
			if(newv>curv)
				return true;
			else if(curv>newv)
				return false;
		}
		return false;
	}

	private int[] splitVersion(String version){
		final String[] tmp = version.split("\\.");
		final int size=tmp.length;
		final int[] out = new int[size];
		for(int i=0;i<size;i++){
			out[i]=Integer.parseInt(tmp[i]);
		}
		return out;
	}

	private void tryNotifyServer(){
		if(isHigherVersion())
			notifyServer();
	}
	private void tryNotifyDynious(){
		if(isHigherVersion()){
			NBTTagCompound update = new NBTTagCompound();
			update.setString("modDisplayName", HostMODNAME);
			update.setString("oldVersion", HostVERSION);
			update.setString("newVersion", latest);
			update.setString("updateURL", downloadURL);
			update.setBoolean("isDirectLink", false);
			FMLInterModComms.sendRuntimeMessage(HostMODID, "VersionChecker", "addUpdate", update);
		}
	}

	private String stringAt(String url){
		String output = "";
		URLConnection con;
		try{
			con = new URL(url).openConnection();
			if(con != null){
				final BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String input;
				StringBuffer buf = new StringBuffer();
				while((input=br.readLine()) != null){
					buf.append(input);
				}
				output = buf.toString();
				br.close();
			}
		}catch(MalformedURLException e){
			return "0.0.0.0";
		}catch(IOException e){
			return "0.0.0.0";
		}
		return output;
	}
}