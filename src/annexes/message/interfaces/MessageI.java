package annexes.message.interfaces;

import java.io.Serializable;

import annexes.message.Properties;
import annexes.message.TimeStamp;

/**
 * 
 * @author Group LAMA
 *
 */
public interface MessageI extends Serializable{
	public String getURI();
	public TimeStamp getTimeStamp();
	public Properties getProperties();
	public Serializable getPayload();

}
