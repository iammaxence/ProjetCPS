package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ReceptionCI;
import annexes.message.interfaces.MessageI;

/**
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
