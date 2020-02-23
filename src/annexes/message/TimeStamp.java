package annexes.message;


public class TimeStamp {
	protected long time;
	protected String timestamper;
	
	public TimeStamp(long time, String timestamper) {
		this.time = time;
		this.timestamper = timestamper;
	}
	
	public TimeStamp() {
		time = time -1; //Par défault
		timestamper = null;
	}
	
	public boolean isInitialised() {
		return (time != -1 && timestamper != null);
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time=time;
	}
	
	public String getTimestamper() {
		return timestamper;
	}
	
	public void setTimestamper(String timestamper) {
		this.timestamper=timestamper;
	}
	
	
}
