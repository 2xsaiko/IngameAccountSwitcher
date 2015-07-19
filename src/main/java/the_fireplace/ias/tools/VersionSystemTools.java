package the_fireplace.ias.tools;

/**
 * @author The_Fireplace
 */

public class VersionSystemTools {
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

		for (int i = 0; i < Math.max(_current.length, _new.length); i++) {
			int curv = 0;
			if (i < _current.length)
				curv = _current[i];
			
			int newv = 0;
			if (i < _new.length)
				newv = _new[i];
			
			if (newv > curv) {
				return true;
			} else if (curv > newv) {
				return false;
			} else {
				//go on
			}
		}
		return false;
	}

	/**
	 * Splits a version in to its number components (Format ".\d+\.\d+\.\d+.*" )
	 *
	 * @param Version
	 *            The version to be split
	 * @return The numeric version components as an integer array
	 */
	private static int[] splitVersion(String version) {
		final String[] tmp = version.split("\\.");
		final int size = tmp.length;
		final int out[] = new int[size];

		for (int i = 0; i < size; i++) {
			out[i] = Integer.parseInt(tmp[i]);
		}

		return out;
	}
}
