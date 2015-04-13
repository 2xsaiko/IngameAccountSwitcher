package com.github.mrebhan.ingameaccountswitcher.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Contains a bunch of tools that didn't fit in another class.
 * @author mr
 * @author The_Fireplace
 */

public class Tools {

	private static String field_388399_c = null;

	public static void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
		Gui.drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
		Gui.drawRect(x + size, y + size, x1, y, borderC);
		Gui.drawRect(x, y, x + size, y1, borderC);
		Gui.drawRect(x1, y1, x1 - size, y + size, borderC);
		Gui.drawRect(x, y1 - size, x1, y1, borderC);
	}

	public static boolean contains(Object[] array, Object target) {
		boolean y = false;

		for (Object object : array) {
			if (object == target || object.equals(target)) {
				y = true;
			}
		}

		return y;
	}

	public static int getNextNullIndex(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null)
				return i;
		}
		return -1;
	}

	public static int indexOf(Object[] array, Object target) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == target || array[i].equals(target))
				return i;
		}
		return -1;
	}
	
	public static String join(String[] var2, String string) {
		// TODO Auto-generated method stub
		String var1 = "";
		for (int i = 0; i < var2.length; i++) {
			var1 += var2[i] + string;
		}
		var1 = var1.substring(0, var1.length() - string.length());
		return var1;
	}

	public static boolean instanceOf(Class<?> a, Class<?> b) {
		return a.getCanonicalName().equals(b.getCanonicalName());
	}

	public static String[] trim(String[] par1) {
		int i = 0;
		for (int j = 0; j < par1.length; j++) {
			if (par1[j] != null)
				if (par1[j] != null && !par1[j].equals("")) {
					i++;
				}
		}
		String[] var11 = new String[i];
		int j = 0;
		int x = 0;
		for (int k = 0; k < par1.length; k++) {
			if (par1[j] != null && !par1[j].equals("")) {
				var11[x] = par1[j];	
				x++;
			}
			j++;
		}
		return var11;
	}
	
	/**
	 * Checks if the new version is higher than the current one
	 * 
	 * @param currentVersion
	 *            The version which is considered current
	 * @param newVersion
	 *            The version which is considered new
	 * @return Whether the new version is higher than the current one or not
	 */
	public static boolean isHigherVersion(String currentVersion,
			String newVersion) {
		final int[] _current = splitVersion(currentVersion);
		final int[] _new = splitVersion(newVersion);

		return (_current[0] < _new[0])
				|| ((_current[0] == _new[0]) && (_current[1] < _new[1]))
				|| ((_current[0] == _new[0]) && (_current[1] == _new[1]) && (_current[2] < _new[2]))
				|| ((_current[0] == _new[0]) && (_current[1] == _new[1]) && (_current[2] == _new[2]) && (_current[3] < _new[3]));
	}

	/**
	 * Splits a version in to its number components (Format ".\d+\.\d+\.\d+.*" )
	 * 
	 * @param Version
	 *            The version to be split (Format is important!)
	 * @return The numeric version components as an integer array
	 */
	private static int[] splitVersion(String Version) {
		final String[] tmp = Version.split("\\.");
		final int size = tmp.length;
		final int out[] = new int[size];

		for (int i = 0; i < size; i++) {
			out[i] = Integer.parseInt(tmp[i]);
		}

		return out;
	}
}
