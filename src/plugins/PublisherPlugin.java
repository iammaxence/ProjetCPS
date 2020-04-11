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
import ports.ManagementCOutBoundPort;
import ports.PublicationCOutBoundPort;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class PublisherPlugin 
extends AbstractPlugin 
implements PublicationsImplementationI, ManagementImplementationI{

	private static final long serialVersionUID = 1L;
	
	protected String URI_BROKER;
	protected ManagementCOutBoundPort     managementOutboundPort;
	protected PublicationCOutBoundPort publicationOutboundPort;
	
	public PublisherPlugin(String URI_BROKER) {
		this.URI_BROKER=URI_BROKER;
	}
	

	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/	
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner) ;
		assert owner != null;

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
				URI_BROKER, // Changement pour DCVM
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
	
	
	/**======================= PUBLICATIONCI ===========================*/
	
	/**
	 * Method of PublicationCI: publish a message for a topic
	 * @param m : The message to send
	 * @param topic : The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.publicationOutboundPort.publish(m, topic);
	}

	/**
	 * Method of PublicationCI: publish a message for a list of topics
	 * @param m : The message to send
	 * @param topics : The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.publicationOutboundPort.publish(m, topics);
	}

	/**
	 * Method of PublicationCI: publish messages for a topic
	 * @param ms : Messages to send
	 * @param topic The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.publicationOutboundPort.publish(ms, topic);
	}

	/**
	 * Method of PublicationCI: publish messages for a list of topics
	 * @param ms : Messages to send
	 * @param topics : The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.publicationOutboundPort.publish(ms, topics);
	}
	
	
	/**============================ MANAGEMENTCI =============================*/
	
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that will be created
	 * @throws Exception 
	 */
	@Override
	public void createTopic(String topic) throws Exception {
		this.managementOutboundPort.createTopic(topic);
	}

	/**
	 * Method of ManagementCI
	 * @param topics that will be created
	 * @throws Exception 
	 */
	@Override
	public void createTopics(String[] topics) throws Exception {
		this.managementOutboundPort.createTopics(topics);
	}

	/**
	 * Method of ManagementCI
	 * @param topic that will be destroy
	 * @throws Exception 
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		this.managementOutboundPort.destroyTopic(topic);
	}

	/**
	 * Method of ManagementCI
	 * @param topic that we are looking for
	 * @return if the topic exist
	 * @throws Exception 
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.managementOutboundPort.isTopic(topic);
	}

	/**
	 * Method of ManagementCI
	 * @return The list of topics that already exist
	 * @throws Exception 
	 */
	@Override
	public String[] getTopics() throws Exception {
		return this.managementOutboundPort.getTopics();
	}
	
}