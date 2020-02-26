package annexes.message;

import java.io.Serializable;

import annexes.message.interfaces.MessageI;

/**
 * 
 * Class that represent a Message in our system
 *
 */
public class Message 
implements MessageI{
	private static final long serialVersionUID = 1L;
	private static int cpt=1;
	
	private final String uri;
	private String message;
	private TimeStamp ts;
	private Properties properties;
	
	/**
	 * Constructor of Message
	 * @param msg: the message -> String
	 */
	public Message(String msg) {
		uri = "Msg"+cpt++;       //ex: Msg12 avec id unique
		ts = new TimeStamp(); 
		this.message = msg;
		properties = new Properties();
	}
	
	/**
	 * Constructor of Message
	 * @param msg: the message -> String
	 * @param ts -> TimeStamp
	 * @param props -> Properties
	 */
	public Message(String msg, TimeStamp ts, Properties props) {
		uri="msg"+cpt++; 
		this.message=msg;
		this.ts=ts;
		properties=props;
	}
	
	/**
	 * Get the URI of the message
	 */
	@Override
	public String getURI() {	
		return uri;
	}

	/**
	 * Get the TimeStamp of the message
	 */
	@Override
	public TimeStamp getTimeStamp() {
		return ts;
	}

	/**
	 * Get Properties of the message
	 */
	@Override
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Get the Payload of the message 
	 */
	@Override
	public Serializable getPayload() {
		return message;
	}

}
