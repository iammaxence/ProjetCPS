package annexes;

import java.util.Date;
import java.util.GregorianCalendar;

public class Time {
	private GregorianCalendar calendar = new GregorianCalendar();
	
	/**
	 * Get the current date 
	 * @return string of the current date
	 */
	public synchronized String getCurrentDate() {
		Date d = calendar.getTime();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss.sss");
		String currentTime = sdf.format(d);
		return currentTime;
	}

}
