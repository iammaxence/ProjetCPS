package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ManagementCI;
import ports.ManagementCInBoundPort;

/**
 * The class <code>BrokerManagementDynamicPlugin</code>
 * represent the dynamic plug-in of the client: a 
 * Subscriber/Publisher (which have to add this plug-in),
 * it will be connected with the server: the Broker in 
 * order to communicate.
 * 
 * @author Group LAMA
 *
 */
public class BrokerManagementDynamicPlugin 
extends DynamicConnectionServerSidePlugin{
	private static final long serialVersionUID = 1L;

	@Override
	protected InboundPortI createAndPublishServerSideDynamicPort(Class<?> offeredInterface) throws Exception 
	{
		assert	ManagementCI.class.isAssignableFrom(offeredInterface) &&
		offeredInterface.isAssignableFrom(ManagementCI.class);

		InboundPortI mibp = new ManagementCInBoundPort(this.owner) ;
		mibp.publishPort() ;
		return mibp;
	}

}
