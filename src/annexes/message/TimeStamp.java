package annexes.message;


public class TimeStamp {
	protected long time;
	protected String timestamper;
	
	/**
	 * Constructor of TimeStamp with argument
	 * @param time
	 * @param timestamper
	 */
	public TimeStamp(long time, String timestamper) {
		this.time = time;
		this.timestamper = timestamper;
	}
	
	/**
	 * Constructor of TimeStamp without argument
	 * time initialize to -1 and timeStamper to null
	 */
	public TimeStamp() {
		time = -1;      //Default value
		timestamper = null;
	}
	
	/**
	 * @return if the time and timeStamper is initialized or not
	 */
	public boolean isInitialised() {
		return (time != -1 && timestamper != null);
	}
	
	/**
	 * @return get the time of the timeStamp-> long
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Set the time of the timeStamp
	 * @param time -> long
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
	 * @param timestamper -> String
	 */
	public void setTimeStamper(String timestamper) {
		this.timestamper=timestamper;
	}
	
	
}
