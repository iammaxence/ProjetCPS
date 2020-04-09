package plugins;

import fr.sorbonne_u.components.plugins.dconnection.DynamicConnectionServerSidePlugin;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.PublicationCI;
import ports.PublicationCInBoundPort;


/**
 * 
 * @author Group LAMA
 *
 */
public class BrokerPublicationDynamicPlugin extends DynamicConnectionServerSidePlugin{
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
