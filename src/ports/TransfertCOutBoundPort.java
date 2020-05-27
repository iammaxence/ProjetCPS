package ports;

import annexes.message.interfaces.MessageI;
import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.TransfertCI;


/**
 * This class <code>TransfertCOutBoundPort</code>
 * represent the out bound port that implement 
 * TransfertImplementationI 
 * 
 * @author GROUP LAMA
 *
 */
public class TransfertCOutBoundPort 
extends AbstractOutboundPort implements TransfertCI{

	private static final long serialVersionUID = 1L;

	public TransfertCOutBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, TransfertCI.class, owner);
		assert	uri != null && owner instanceof Broker;
	}

	@Override
	public void transfererMessage(MessageI msg, String topic) throws Exception {
		((TransfertCI)this.connector).transfererMessage(msg, topic);
	}

}
