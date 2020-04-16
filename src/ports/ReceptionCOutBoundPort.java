package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class ReceptionCOutBoundPort 
extends AbstractOutboundPort implements ReceptionCI{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of ReceptionCOutBoundPort
	 * @param owner of this port
	 * @throws Exception
	 */
	public ReceptionCOutBoundPort(ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
		assert owner instanceof Broker ;
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
