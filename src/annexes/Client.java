package annexes;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import annexes.message.interfaces.MessageFilterI;
import ports.ReceptionCOutBoundPort;

/**
 * This class <code>Client </code> represent the
 * subscription of a Subscriber indeed this class
 * gather all filters for each topic that the
 * subscriber subscribe, the ReceptionCOutBoundPort
 * and the URI of the ReceptionCInBoundPort.
 * 
 * @author Group LAMA
 *
 */
public class Client {
	private String                                             ReceptionCInBoundPortURI;     //inBountPortURI of the Subscriber
	private ReceptionCOutBoundPort                             port;                         //outBoundPort between the Broker and the Subscriber
	private ConcurrentHashMap <String, MessageFilterI>             filters;                      //key: topic value: filter for the topic
	
	/**
	 * Constructor of a Client 
	 * 
	 * pre	port != null
	 * pre  inBoundPortURI != ""
	 * post port.isPublish()
	 * 
	 * @param inBoundPortURI is the URI of Subscriber's port
	 * @param port is the Broker's port linked to the Subscriber
	 */
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port) {
		assert inBoundPortURI != "" && port!=null;
		
		this.ReceptionCInBoundPortURI = inBoundPortURI;
		this.port = port;
		filters = new ConcurrentHashMap <String, MessageFilterI>();
		try {
			this.port.publishPort();
			assert port.isPublished();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		if(filters.containsKey(topic))
			return filters.get(topic);
		return null;
	}

	/**
	 * Set a filter to a particular topic
	 * @param filter of the topic
	 * @param topic where the client want to put a filter 
	 */
	public void setFilter(MessageFilterI filter, String topic) {
		filters.put(topic, filter);
	}
	
	/**
	 * Remove the filter of a topic
	 * @param topic of question
	 */
	public void removeFilterOfTopic(String topic) {
		if(filters.containsKey(topic))
			filters.remove(topic);
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
		return ReceptionCInBoundPortURI;
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
		Client copy = new Client(this.ReceptionCInBoundPortURI, this.port);
		Set<String> topics = filters.keySet();
		for(String topic: topics) {
			copy.setFilter(filters.get(topic), topic);
		}
		return copy;
	}
	
	/**
	 * Unpublish the ReceptionCOutBoundPort
	 */
	public void unpublish() {
		try {
			port.unpublishPort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
}
