package the_fireplace.ias.legacysupport;

import java.time.LocalDateTime;

public class NewJava implements ILegacyCompat {
	@Override
	public int[] getDate() {
		int[] ret = new int[3];
		ret[0]=LocalDateTime.now().getMonthValue();
		ret[1]=LocalDateTime.now().getDayOfMonth();
		ret[2]=LocalDateTime.now().getYear();
		return ret;
	}

	@Override
	public String getMessage() {
		return "";
	}
}
