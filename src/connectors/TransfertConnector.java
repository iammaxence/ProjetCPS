package connectors;

import annexes.message.interfaces.MessageI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.TransfertCI;

/**
 * This class <code>TransfertConnector</code> represent 
 * the connector between two ports that implement 
 * TransfertImplementationI 
 * 
 * @author Group LAMA
 *
 */
public class TransfertConnector 
extends AbstractConnector implements TransfertCI{

	@Override
	public void transfererMessage(MessageI msg, String topic) throws Exception {
		((TransfertCI)this.offering).transfererMessage(msg, topic);
		
	}

}
