package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ReceptionCI;
import ports.ReceptionCInBoundPort;


/**
 * The class <code>SubscriberReceptionDynamicPlugin</code>
 * represent the dynamic plug-in of the client: the Broker
 * (which have to add this plug-in), it will be connected 
 * with the server: a Subscriber in order to communicate.
 * 
 * @author Group LAMA
 *
 */
public class SubscriberReceptionDynamicPlugin 
extends DynamicConnectionServerSidePlugin{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected InboundPortI createAndPublishServerSideDynamicPort(Class<?> offeredInterface) throws Exception 
	{
		assert ReceptionCI.class.isAssignableFrom(offeredInterface) &&
		offeredInterface.isAssignableFrom(ReceptionCI.class);
		
		InboundPortI portInReception = new ReceptionCInBoundPort(this.owner);
		portInReception.publishPort();
		return portInReception;
	}
}
