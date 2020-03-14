package annexes.message;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;

/**
 * 
 * @author Group LAMA
 *
 */
public class TimeStamp {
	protected long time;
	protected String timestamper;
	
	/**
	 * Constructor of TimeStamp with argument
	 * @param time : time system of Unix -> long
	 * @param timestamper : identification of the guest -> String 
	 */
	public TimeStamp(long time, String timestamper) {
		this.time = time;
		this.timestamper = timestamper;
	}
	
	/**
	 * Constructor of TimeStamp without argument
	 * time initialize to the number of milliseconds from 1970-01-01 UTC until now UTC 
	 * and timeStamper to the hostname of the current server
	 */
	public TimeStamp() {
		time = Instant.now().toEpochMilli();
		try {
			timestamper = InetAddress.getLocalHost().getHostAddress(); 
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return if the time and timeStamper is initialized or not
	 */
	public boolean isInitialised() {
		return (time < 0 && timestamper != null);
	}
	
	/**
	 * @return get the time of the timeStamp-> long
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Set the time of the timeStamp
	 * @param time : time system of Unix -> long
	 */
	public void setTime(long time) {
		this.time=time;
	}

	/**
	 * @return get the timeStamper-> String
	 */
	public String getTimeStamper() {
		return timestamper;
	}
	
	/**
	 * Set the timeStamper
	 * @param timestamper : name of the guest -> String
	 */
	public void setTimeStamper(String timestamper) {
		this.timestamper=timestamper;
	}
	
	
}
