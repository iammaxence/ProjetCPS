package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import interfaces.ManagementCI;
import ports.ManagementCInBoundPort;


public class BrokerDynamicPlugin 
extends DynamicConnectionServerSidePlugin{


	private static final long serialVersionUID = 1L;

	@Override
	protected ManagementCInBoundPort createAndPublishServerSideDynamicPort(Class<?> offeredInterface) throws Exception {
		assert	ManagementCI.class.isAssignableFrom(offeredInterface) &&
		offeredInterface.isAssignableFrom(ManagementCI.class) ;

		ManagementCInBoundPort ret = new ManagementCInBoundPort(this.owner) ;
		ret.publishPort() ;
		return ret ;
	}

}
