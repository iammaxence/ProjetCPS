package ports;

import annexes.message.interfaces.MessageI;
import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.TransfertImplementationI;

public class TransfertCInBoundPort extends AbstractInboundPort implements TransfertImplementationI{
	
	
	//Pool de thread par la suite ?
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
