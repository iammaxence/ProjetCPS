package ports;

import components.Subscriber;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class ReceptionCInBoundPort 
extends AbstractInboundPort implements ReceptionCI{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ReceptionCInBoundPort
	 * @param uri of the port
	 * @param owner of the port
	 * @throws Exception
	 */
	public ReceptionCInBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		assert uri!=null && owner instanceof Subscriber ;
	}
	
	/**
	 * Constructor of ReceptionCInBoundPort
	 * @param owner of this port
	 * @throws Exception
	 */
	public ReceptionCInBoundPort(ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
		assert owner instanceof Subscriber ;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().handleRequestAsync(
					owner -> {((Subscriber)owner).acceptMessage(m); return null;}) ;
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		this.getOwner().handleRequestAsync(
					owner -> {((Subscriber)owner).acceptMessages(ms); return null;}) ;
	}

}
