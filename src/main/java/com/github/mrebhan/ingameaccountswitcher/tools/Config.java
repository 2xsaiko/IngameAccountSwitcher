package com.github.mrebhan.ingameaccountswitcher.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import the_fireplace.iasencrypt.Standards;
/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class Config implements Serializable {
	public static final long serialVersionUID = 0xDEADBEEF;

	private static Config instance = null;

	private static final String configFileName = Standards.cfgn;

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
		if(field_218893_c == null){
			System.out.println("Error: Config failed to load during PreInitialization. Loading now.");
			load();
		}
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
		loadFromOld();
		saveToFile();
	}

	private static void saveToFile() {//Shouldn't this and getFromFile be switched? This gets data from the file.
		File f = new File(Minecraft.getMinecraft().mcDataDir+Standards.IASFOLDER, configFileName);
		if (f.exists()) {
			try {
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
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

	private static void getFromFile() {//Shouldn't this and saveToFile be switched? This saves data to the file.
		try{
			Path file = new File(Minecraft.getMinecraft().mcDataDir+Standards.IASFOLDER, configFileName).toPath();
			DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class);
			DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class);
			if(attr.isHidden())
				view.setHidden(false);
		}catch(Exception e){
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir+Standards.IASFOLDER, configFileName)));
			out.writeObject(instance);
			out.close();
		} catch (IOException e) {
		}
		try{
			Path file = new File(Minecraft.getMinecraft().mcDataDir+Standards.IASFOLDER, configFileName).toPath();
			DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class);
			DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class);
			if(!attr.isHidden())
				view.setHidden(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void loadFromOld(){
		File f = new File(Minecraft.getMinecraft().mcDataDir+Standards.IASFOLDER, "user.cfg");
		if (f.exists()) {
			try {
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
				instance = (Config) stream.readObject();
				stream.close();
				f.delete();
				System.out.println("Loaded data from old file");
			} catch (IOException e) {
				e.printStackTrace();
				f.delete();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				f.delete();
			}
		}
	}
}
