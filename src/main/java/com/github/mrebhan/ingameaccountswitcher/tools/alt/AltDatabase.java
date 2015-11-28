package com.github.mrebhan.ingameaccountswitcher.tools.alt;

import java.io.Serializable;
import java.util.ArrayList;

import com.github.mrebhan.ingameaccountswitcher.tools.Config;
import com.github.mrebhan.ingameaccountswitcher.tools.Pair;
import com.github.mrebhan.ingameaccountswitcher.tools.queuing.CallQueue;
import com.github.mrebhan.ingameaccountswitcher.tools.queuing.QueueElement;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class AltDatabase implements Serializable {	

	public static final long serialVersionUID = 0xA17DA7AB;
	private static AltDatabase instance;

	private final ArrayList<AccountData> altList;

	private AltDatabase() {
		this.altList = new ArrayList();
	}

	private static void loadFromConfig() {
		if (instance == null)
			instance = (AltDatabase) Config.getInstance().getKey("altaccounts");
	}

	private static void saveToConfig() {
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
