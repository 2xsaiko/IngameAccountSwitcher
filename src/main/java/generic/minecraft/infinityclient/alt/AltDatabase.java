package generic.minecraft.infinityclient.alt;

import java.io.Serializable;
import java.util.ArrayList;

import generic.minecraft.infinityclient.Config;
import generic.minecraft.infinityclient.Pair;
import generic.minecraft.infinityclient.queuing.CallQueue;
import generic.minecraft.infinityclient.queuing.QueueElement;

public class AltDatabase implements Serializable {	
	
	public static final long serialVersionUID = 0xA17DA7AB;
	private static AltDatabase instance;
	
	private ArrayList<AccountData> altList;
	
	private AltDatabase() {
		this.altList = new ArrayList();
	}
	
	public static void loadFromConfig() {
		if (instance == null)
			instance = (AltDatabase) Config.getInstance().getKey("altaccounts");
	}
	
	public static void saveToConfig() {
		Config.getInstance().setKey(new Pair<String, Object>("altaccounts", instance));
	}
	
	public static AltDatabase getInstance() {
		loadFromConfig();
		if (instance == null) {
			instance = new AltDatabase();
			saveToConfig();
		}
		return instance;
	}
	
	public ArrayList<AccountData> getAlts() {
		CallQueue.addToQueue(new QueueElement(1) {
			
			@Override
			public void onCall() {
				AltDatabase.saveToConfig();
			}
		});
		return this.altList;
	}
}
