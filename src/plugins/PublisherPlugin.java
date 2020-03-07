package plugins;

import annexes.message.interfaces.MessageI;
import connectors.ManagementConnector;
import connectors.PublicationConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import launcher.CVM;
import ports.ManagementCOutBoundPort;
import ports.PublicationCOutBoundPort;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class PublisherPlugin 
extends AbstractPlugin implements PublicationsImplementationI, ManagementImplementationI{

	private static final long serialVersionUID = 1L;
	
	protected ManagementCOutBoundPort     managementOutboundPort;
	protected PublicationCOutBoundPort publicationOutboundPort;
	

	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/	
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner) ;

		/**----------------- ADD COMPONENTS -------------------*/
		this.addRequiredInterface(PublicationCI.class) ;
		this.addRequiredInterface(ManagementCI.class) ;
		
		/**---------------- PORTS CREATION --------------------*/
		this.publicationOutboundPort = new PublicationCOutBoundPort(this.owner);
		this.managementOutboundPort = new ManagementCOutBoundPort(this.owner);
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.publicationOutboundPort.publishPort() ;
		this.managementOutboundPort.publishPort();
	}

	
	@Override
	public void initialise() throws Exception {
		// Use the reflection approach to get the URI of the inbound port
		
		this.addRequiredInterface(ReflectionI.class) ;
		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner) ;
		rop.publishPort() ;
		
		/**-------------- BrokerPublicationCI --------------------*/
		this.owner.doPortConnection(
				rop.getPortURI(),
				CVM.BROKER_COMPONENT_URI,
				ReflectionConnector.class.getCanonicalName()) ;
		
		String[] urip = rop.findPortURIsFromInterface(PublicationCI.class) ;
		assert	urip != null && urip.length == 1 ;
		
		/**-------------- BrokerManagementCI --------------------*/
		
		String[] urim = rop.findPortURIsFromInterface(ManagementCI.class) ;
		assert	urim != null && urim.length == 1 ;
		
		/**----------- DISCONNECT&DESTROY REFLECTION -----------*/
		this.owner.doPortDisconnection(rop.getPortURI()) ;
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;

		/**------------------ CONNECT PORTS --------------------*/
		this.owner.doPortConnection(
				this.publicationOutboundPort.getPortURI(),
				urip[0],
				PublicationConnector.class.getCanonicalName()) ;
		
		this.owner.doPortConnection(
				this.managementOutboundPort.getPortURI(),
				urim[0],
				ManagementConnector.class.getCanonicalName()) ;

		super.initialise();
	}
	
	
	@Override
	public void finalise() throws Exception {
		this.owner.doPortDisconnection(this.publicationOutboundPort.getPortURI()) ;
		this.owner.doPortDisconnection(this.managementOutboundPort.getPortURI()) ;
	}

	
	@Override
	public void	uninstall() throws Exception {
		
		this.publicationOutboundPort.unpublishPort() ;
		this.managementOutboundPort.unpublishPort();
		
		this.publicationOutboundPort.destroyPort() ;
		this.managementOutboundPort.destroyPort();
		
		this.removeRequiredInterface(PublicationCI.class) ;
		this.removeRequiredInterface(ManagementCI.class) ;
	}
	
	
	
	/**------------------------------------------------------------------
	 * ---------------- PLUGIN SERVICES IMPLEMENTATION ------------------
	 ------------------------------------------------------------------*/
	
	
	/**======================================================================================
	 * ================================== PUBLICATIONCI =====================================
	 ======================================================================================*/
	
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.publicationOutboundPort.publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.publicationOutboundPort.publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.publicationOutboundPort.publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.publicationOutboundPort.publish(ms, topics);
	}
	
	/**======================================================================================
	 * =================================== MANAGEMENTCI =====================================
	 ======================================================================================*/

	@Override
	public void createTopic(String topic) throws Exception {
		this.managementOutboundPort.createTopic(topic);
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		this.managementOutboundPort.createTopics(topics);
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		this.managementOutboundPort.destroyTopic(topic);
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.managementOutboundPort.isTopic(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.managementOutboundPort.getTopics();
	}
	
}
