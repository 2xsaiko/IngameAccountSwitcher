package com.github.mrebhan.ingameaccountswitcher.tools;

/**
 * @author The_Fireplace
 */

public class Tools {
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
