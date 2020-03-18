package annexes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import annexes.message.interfaces.MessageFilterI;
import ports.ReceptionCOutBoundPort;

/**
 * 
 * @author Group LAMA
 *
 */
public class Client {
	private String ReceptionCInBoundPort;
	private ReceptionCOutBoundPort port;
	private Map <String, MessageFilterI> filters; //<Topic, Filter>
	
	/**
	 * Constructor Client 
	 * @param inBoundPortURI is the URI of Subscriber's port
	 * @param port is the Broker's port linked to the Subscriber
	 */
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port) {
		ReceptionCInBoundPort = inBoundPortURI;
		this.port = port;
		filters = new HashMap<String, MessageFilterI>();
	}

	/**
	 * Get the OutBoundPort
	 * @return the Broker's port
	 */
	public ReceptionCOutBoundPort getPort() {
		return port;
	}
	
	/**
	 * @param topic in question
	 * @return check if the topic has a filter
	 */
	public boolean hasFilter(String topic) {
		return filters.containsKey(topic);
	}

	/**
	 * Get the MessageFilterI
	 * @param topic in question
	 * @return the filter put for this topic
	 */
	public MessageFilterI getFilter(String topic) {
		if(hasFilter(topic))
			return filters.get(topic);
		return null;
	}

	/**
	 * Set a filter to a particular topic
	 * @param filter of the topic
	 * @param topic where the client want to put a filter 
	 */
	public void setFilter(MessageFilterI filter, String topic) {
		filters.put( topic, filter);
	}
	
	/**
	 * Get the URI of the OutBoundPort
	 * @return the URI of the Broker's port
	 */
	public String getOutBoundPortURI() {
		try {
			return port.getPortURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get the URI of InBoundPort
	 * @return the URI of the Subscriber's port
	 */
	public String getInBoundPortURI() {
		return ReceptionCInBoundPort;
	}
	
	/**
	 * @param obj is the object to compare 
	 * @return if the object is equal to our client 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) 
			return false;
		if(obj instanceof Client) {
			Client sub = (Client) obj;
			if(sub.getInBoundPortURI().equals(this.getInBoundPortURI()))
				return true;
		}
		return false;
	}
	
	/**
	 * @return a copy of a client
	 */
	public Client copy() {
		Client copy = new Client(this.ReceptionCInBoundPort, this.port);
		Set<String> topics = filters.keySet();
		for(String topic: topics) {
			copy.setFilter(filters.get(topic), topic);
		}
		return copy;
	}
		
	
}
