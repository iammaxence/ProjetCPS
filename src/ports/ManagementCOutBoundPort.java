package ports;

import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ManagementCI;
import annexes.message.interfaces.MessageFilterI;

/**
 * This class <code>ManagementCOutBoundPort</code>
 * represent the out bound port that implement 
 * ManagementCI 
 * 
 * @author GROUP LAMA
 *
 */
public class ManagementCOutBoundPort 
extends AbstractOutboundPort implements ManagementCI{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ManagementCOutBoundPort
	 * 
	 * pre (owner instanceof Subscriber || owner instanceof Publisher)
	 * 
	 * @param owner of the port
	 * @throws Exception
	 */
	public ManagementCOutBoundPort(ComponentI owner) throws Exception {
		super(ManagementCI.class, owner);
		assert (owner instanceof Subscriber || owner instanceof Publisher);
	}
	

	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, inboundPortUri);	
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topics, inboundPortUri);	
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, filter, inboundPortUri);
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).modifyFilter(topic, newFilter, inboundPortUri);
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).unsubscribe(topic, inboundPortURI);
	}

	@Override
	public void createTopic(String topic) throws Exception {
		((ManagementCI)this.connector).createTopic(topic);
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		((ManagementCI)this.connector).createTopics(topics);		
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.connector).destroyTopic(topic);
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.connector).isTopic(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		return ((ManagementCI)this.connector).getTopics();
	}

}
