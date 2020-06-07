package ports;

import components.Publisher;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.PublicationCI;
import annexes.message.interfaces.MessageI;

/**
 * This class <code>PublicationCOutBoundPort</code>
 * represent the out bound port that implement 
 * PublicationCI 
 * 
 * @author GROUP LAMA
 *
 */
public class PublicationCOutBoundPort 
extends AbstractOutboundPort implements PublicationCI{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of PublicationCOutBoundPort
	 * 
	 * @pre  owner instanceof Publisher
	 * 
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
