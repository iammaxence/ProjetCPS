package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.PublicationCI;
import ports.PublicationCInBoundPort;


/**
 * The class <code>BrokerPublicationDynamicPlugin</code>
 * represent the dynamic plug-in of the client: a Publisher
 * (which have to add this plug-in), it will be connected 
 * with the server: the Broker in order to communicate.
 * 
 * @author Group LAMA
 *
 */
public class BrokerPublicationDynamicPlugin 
extends DynamicConnectionServerSidePlugin{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected InboundPortI createAndPublishServerSideDynamicPort(Class<?> offeredInterface) throws Exception 
	{
		assert	PublicationCI.class.isAssignableFrom(offeredInterface) &&
		offeredInterface.isAssignableFrom(PublicationCI.class);
		
		InboundPortI pibp = new PublicationCInBoundPort(this.owner);
		pibp.publishPort();
		return pibp;
	}

}
