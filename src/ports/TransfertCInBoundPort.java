package ports;

import annexes.message.interfaces.MessageI;
import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.TransfertImplementationI;


/**
 * This class <code>TransfertCInBoundPort</code>
 * represent the in bound port that implement 
 * TransfertImplementationI 
 * 
 * @author GROUP LAMA
 *
 */
public class TransfertCInBoundPort 
extends AbstractInboundPort implements TransfertImplementationI{
	
	private static final long serialVersionUID = 1L;
	
	//Pool de thread par la suite ?
	

	
	public TransfertCInBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, TransfertImplementationI.class, owner);
		assert	uri != null && owner instanceof Broker ;

	}

	@Override
	public void transfererMessage(MessageI msg, String topic) throws Exception {
		this.getOwner().handleRequestSync(
				owner -> {((Broker)owner).transfererMessage(msg, topic);; return null;}) ;
		
	}

}
