package ports;

import components.Publisher;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.PublicationCI;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class PublicationCOutBoundPort 
extends AbstractOutboundPort implements PublicationCI{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of PublicationCOutBoundPort
	 * @param uri of the port
	 * @param owner of the port
	 * @throws Exception
	 */
	public PublicationCOutBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
		assert	uri != null && owner instanceof Publisher;
	}
	
	/**
	 * Constructor of PublicationCOutBoundPort
	 * @param owner of the port
	 * @throws Exception
	 */
	public PublicationCOutBoundPort(ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
		assert owner instanceof Publisher;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.connector).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		((PublicationCI)this.connector).publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(ms, topics);
	}

}
