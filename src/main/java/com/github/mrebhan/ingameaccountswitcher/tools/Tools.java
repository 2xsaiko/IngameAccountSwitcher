package com.github.mrebhan.ingameaccountswitcher.tools;

import net.minecraft.client.gui.Gui;

/**
 * @author mr
 * @author The_Fireplace
 */

public class Tools {

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
}
