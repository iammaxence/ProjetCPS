package ports;

import components.Subscriber;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

/**
 * This class <code>ReceptionCInBoundPort</code>
 * represent the in bound port that implement 
 * ReceptionCI 
 * 
 * @author GROUP LAMA
 *
 */
public class ReceptionCInBoundPort 
extends AbstractInboundPort implements ReceptionCI{
	
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor of ReceptionCInBoundPort
	 * 
	 * pre owner instanceof Subscriber
	 * 
	 * @param owner of this port
	 * @throws Exception
	 */
	public ReceptionCInBoundPort(ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
		assert owner instanceof Subscriber ;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Subscriber)owner).acceptMessage(m); return null;}) ;
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Subscriber)owner).acceptMessages(ms); return null;}) ;
	}

}
