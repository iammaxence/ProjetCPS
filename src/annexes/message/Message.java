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
	private Serializable contenue;
	private TimeStamp ts;
	private Properties properties;
	
	/**
	 * Constructor of Message
	 * @param contenue: here a message -> Serializable
	 */
	public Message(Serializable contenue) {
		uri = "MESSAGE_"+cpt++;       //ex: MESSAGE_1 qui est donc unique
		ts = new TimeStamp(); 
		this.contenue = contenue;
		properties = new Properties();
	}
	
	/**
	 * Constructor of Message
	 * @param contenue: here a message -> Serializable
	 * @param ts: describe time and guest -> TimeStamp
	 * @param props: map of properties -> Properties
	 */
	public Message(Serializable contenue, TimeStamp ts, Properties props) {
		uri="MESSAGE_"+cpt++; 
		this.contenue=contenue;
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
		return contenue;
	}

}
