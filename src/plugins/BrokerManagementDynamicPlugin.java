package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ManagementCI;
import ports.ManagementCInBoundPort;

/**
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
