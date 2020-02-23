package annexes;


import annexes.message.interfaces.MessageFilterI;
import ports.ReceptionCOutBoundPort;

public class Subscription {
	private ReceptionCOutBoundPort port;
	private MessageFilterI filter;
	
	public Subscription(ReceptionCOutBoundPort port) {
		this.port = port;
		filter = null;
	}
	
	public Subscription(ReceptionCOutBoundPort port, MessageFilterI filter) {
		this.port = port;
		this.filter = filter;
	}
	
	public ReceptionCOutBoundPort getPort() {
		return port;
	}
	
	public boolean hasFilter() {
		if(filter != null)
			return true;
		return false;
	}

	public MessageFilterI getFilter() {
		return filter;
	}

	public void setFilter(MessageFilterI newfilter) {
		this.filter = newfilter;
	}
	
	public String getUri() {
		try {
			return port.getPortURI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param Object o
	 * @return 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) 
			return false;
		if(obj instanceof Subscription) {
			Subscription sub = (Subscription) obj;
			if(sub.getUri().equals(this.getUri()))
				return true;
		}
		return false;
	}
		
	
}
