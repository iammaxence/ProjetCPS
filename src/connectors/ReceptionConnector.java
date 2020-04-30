package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

/**
 * This class <code>ReceptionConnector</code> represent 
 * the connector between two ports that implement ReceptionCI
 * 
 * @author GROUP LAMA
 *
 */
public class ReceptionConnector 
extends AbstractConnector implements ReceptionCI {

	@Override
	public void acceptMessage(MessageI m) throws Exception{
		((ReceptionCI)this.offering).acceptMessage(m);
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception{
		((ReceptionCI)this.offering).acceptMessages(ms);
	}

}
