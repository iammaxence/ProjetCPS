package ports;

import components.Subscriber;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

public class ReceptionCInBoundPort 
extends AbstractInboundPort implements ReceptionCI{
	private static final long serialVersionUID = 1L;

	public ReceptionCInBoundPort(String uri,ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		assert	uri != null && owner instanceof Subscriber ;
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
