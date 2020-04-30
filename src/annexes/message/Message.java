package annexes.message;

import java.io.Serializable;

import annexes.message.interfaces.MessageI;

/**
 * This class <code>Message</code> represent
 * a message by a serializable content with
 * properties and an unique id.
 * 
 * @author Group LAMA
 *
 */
public class Message implements MessageI{
	
	private static final long serialVersionUID = 1L;
	private static int cpt = 1;
	
	private final String uri;
	private Serializable contenue;
	private TimeStamp ts;
	private Properties properties;
	
	/**
	 * Constructor of Message
	 * @param contenue : here a message -> Serializable
	 */
	public Message(Serializable contenue) {
		uri = generateURI();       //ex: MESSAGE_1 qui est donc unique
		ts = new TimeStamp(); 
		this.contenue = contenue;
		properties = new Properties();
		initializeProps();
	}
	
	/**
	 * Constructor of Message
	 * 
	 * pre properties != null
	 * pre timestamp != null
	 * 
	 * 
	 * @param contenue is here a message -> Serializable
	 * @param ts describe time and guest -> TimeStamp
	 * @param props is a map of properties -> Properties
	 */
	public Message(Serializable contenue, TimeStamp ts, Properties props) {
		assert ts!= null && props != null;
				
		uri = generateURI();       //ex: MESSAGE_1 qui est donc unique
		this.contenue=contenue;
		this.ts=ts;
		properties=props;
	}
	
	/**
	 * Generate an unique message's URI (only access to cpt)
	 * @return URI of the message
	 */
	private synchronized String generateURI() {
		return "MESSAGE_"+ cpt++;
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
	
	/**
	 * Initialize the properties with the toString of the serializable
	 */
	private void initializeProps() {
		String str = contenue.toString();
		String[] mots = str.split("[, ?.@!]+"); 
		for(String mot: mots) {
			properties.putProp(mot,true);
		}
		
	}

}
