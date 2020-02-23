package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

public class ReceptionCOutBoundPort 
extends AbstractOutboundPort implements ReceptionCI{
	private static final long serialVersionUID = 1L;

	public ReceptionCOutBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, owner);
		assert	uri != null && owner instanceof Broker ;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionCI)this.connector).acceptMessage(m);
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionCI)this.connector).acceptMessages(ms);
	}
	
}
