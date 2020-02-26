package annexes;


import annexes.message.interfaces.MessageFilterI;
import ports.ReceptionCOutBoundPort;

public class Client {
	private String ReceptionCInBoundPort;
	private ReceptionCOutBoundPort port;
	private MessageFilterI filter;
	
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port) {
		ReceptionCInBoundPort = inBoundPortURI;
		this.port = port;
		filter = null;
	}
	
	public Client(String inBoundPortURI, ReceptionCOutBoundPort port, MessageFilterI filter) {
		ReceptionCInBoundPort = inBoundPortURI;
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
