package annexes;


import java.util.HashMap;
import java.util.Map;

import annexes.message.interfaces.MessageFilterI;
import ports.ReceptionCOutBoundPort;

public class Client {
	private String ReceptionCInBoundPort;
	private ReceptionCOutBoundPort port;
	private Map <String, MessageFilterI> filters;
	
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port) {
		ReceptionCInBoundPort = inBoundPortURI;
		this.port = port;
		filters = new HashMap<String, MessageFilterI>();
	}
	
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port, Map <String, MessageFilterI> filters) {
		ReceptionCInBoundPort = inBoundPortURI;
		this.port = port;
		this.filters = filters;
	}
	
	public ReceptionCOutBoundPort getPort() {
		return port;
	}
	
	public boolean hasFilter(String topic) {
		return filters.containsKey(topic);
	}

	public MessageFilterI getFilter(String topic) {
		if(hasFilter(topic))
			return filters.get(topic);
		return null;
	}

	public void setFilter(MessageFilterI newfilter, String topic) {
		filters.put( topic, newfilter);
	}
	
	public String getOutBoundPortURI() {
		try {
			return port.getPortURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getInBoundPortURI() {
		return ReceptionCInBoundPort;
	}
	
	/**
	 * @param Object o
	 * @return 
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
		
	
}
