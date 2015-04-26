package com.github.mrebhan.ingameaccountswitcher.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class Config implements Serializable {
	public static final long serialVersionUID = 0xDEADBEEF;

	private static Config instance = null;

	private static final String configFileName = "user.cfg";

	private ArrayList<Pair<String, Object>> field_218893_c;

	public static Config getInstance() {		
		return instance;
	}

	public Config() {
		this.field_218893_c = new ArrayList<Pair<String, Object>>();
		this.instance = this;
	}

	public void setKey(Pair<String, Object> key) {
		if (this.getKey(key.getValue1()) != null)
			this.removeKey(key.getValue1());
		field_218893_c.add(key);
		this.save();
	}

	public void setKey(String key, Object value) {
		this.setKey(new Pair<String, Object>(key, value));
	}

	public void clear() {
		this.field_218893_c = new ArrayList<Pair<String, Object>>();
	}

	public Object getKey(String key) {
		for (int i = 0; i < field_218893_c.size(); i++) {
			if (field_218893_c.get(i).getValue1().equals(key))
				return field_218893_c.get(i).getValue2();
		}

		return null;
	}

	public void removeKey(String key) {
		for (int i = 0; i < field_218893_c.size(); i++) {
			if (field_218893_c.get(i).getValue1().equals(key))
				field_218893_c.remove(i);
		}
	}

	public static void save() {
		getFromFile();
	}

	public static void load() {
		saveToFile();
	}

	private static void saveToFile() {
		File f = new File(Minecraft.getMinecraft().mcDataDir, configFileName);
		if (f.exists()) {
			try {
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(new File(Minecraft.getMinecraft().mcDataDir, configFileName)));
				instance = (Config) stream.readObject();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				instance = new Config();
				f.delete();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				instance = new Config();
				f.delete();
			}
		}
		if (instance == null)
			instance = new Config();
	}

	private static void getFromFile() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, configFileName)));
			out.writeObject(instance);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
