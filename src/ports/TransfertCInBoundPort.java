package ports;

import annexes.message.interfaces.MessageI;
import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.TransfertCI;


/**
 * This class <code>TransfertCInBoundPort</code>
 * represent the in bound port that implement 
 * TransfertImplementationI 
 * 
 * @author GROUP LAMA
 *
 */
public class TransfertCInBoundPort 
extends AbstractInboundPort implements TransfertCI{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor of TransfertCInBoundPort
	 * 
	 * @pre owner instanceof Broker
	 * @pre uri != null
	 * 
	 * @param uri of the component 
	 * @param owner of this port
	 * @throws Exception
	 */
	public TransfertCInBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, TransfertCI.class, owner);
		assert	uri != null && owner instanceof Broker ;

	}

	@Override
	public void transfererMessage(MessageI msg, String topic) throws Exception {
		
		this.getOwner().handleRequestSync(1,
				owner -> {((Broker)owner).transfererMessage(msg, topic);; return null;}) ;
		
	}
	
	

}
