package plugins;

import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;
import connectors.ManagementConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.ReceptionImplementationI;
import interfaces.SubscriptionImplementationI;
import ports.ManagementCOutBoundPort;
import ports.ReceptionCInBoundPort;

/**
 * This class <code>SubscriberPlugin</code>
 * represent the Subscriber plug-in that implement all 
 * the method of SubscriptionImplementationI and
 * ReceptionImplementationI. Beside, all the ports 
 * are publish and connected too. 
 * 
 * @author GROUP LAMA
 *
 */
public class SubscriberPlugin 
extends AbstractPlugin 
implements SubscriptionImplementationI, ReceptionImplementationI{

	private static final long serialVersionUID = 1L;
	protected String URI_BROKER;
	protected ManagementCOutBoundPort managementOutboundPort;
	protected ReceptionCInBoundPort recepetionInboundPort;
	
	public SubscriberPlugin(String URI_BROKER) {
		this.URI_BROKER=URI_BROKER;
	}
	
	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner) ;
		assert owner instanceof ReceptionImplementationI;
		
		/**---------------- PORTS CREATION --------------------*/
		this.managementOutboundPort=new ManagementCOutBoundPort(this.owner);
		this.recepetionInboundPort=new ReceptionCInBoundPort(this.owner);
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.managementOutboundPort.publishPort(); 
		this.recepetionInboundPort.publishPort();

	}
	
	@Override
	public void initialise() throws Exception {
		super.initialise();
		
		this.addRequiredInterface(ReflectionI.class) ;
		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner);
		rop.publishPort() ;
		
		/**-------------- BrokerManagementCI --------------------*/
		this.owner.doPortConnection(
				rop.getPortURI(),
				URI_BROKER,
				ReflectionConnector.class.getCanonicalName()) ;
		
		String[] uris = rop.findPortURIsFromInterface(ManagementCI.class);
		assert	uris != null;

		/**----------- DISCONNECT&DESTROY REFLECTION -----------*/
		this.owner.doPortDisconnection(rop.getPortURI());
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class);

		/**------------------ CONNECT PORT --------------------*/
		this.owner.doPortConnection(
				this.managementOutboundPort.getPortURI(),
				uris[0],
				ManagementConnector.class.getCanonicalName());

		
	}
	
	
	@Override
	public void finalise() throws Exception {
		this.owner.doPortDisconnection(this.managementOutboundPort.getPortURI()) ;
	}

	
	@Override
	public void	uninstall() throws Exception {
		this.managementOutboundPort.unpublishPort() ;
		this.recepetionInboundPort.unpublishPort() ;
		
		this.managementOutboundPort.destroyPort() ;
		this.recepetionInboundPort.destroyPort() ;
		
	}
	
	
	
	/**------------------------------------------------------------------
	 * ---------------- PLUGIN SERVICES IMPLEMENTATION ------------------
	 ------------------------------------------------------------------*/
	/**
	 * @return the URI of the ReceptionCInBoundPort
	 */
	public String getReceptionURI() {
		try {
			return recepetionInboundPort.getPortURI();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**============================ MANAGEMENTCI =============================*/
	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.managementOutboundPort.subscribe(topic, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topics where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param filter of the topic
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		this.managementOutboundPort.subscribe(topic, filter, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to modify his filter
	 * @param newFilter is the new filter 
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		this.managementOutboundPort.modifyFilter(topic, newFilter, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to unsubscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortURI);
	}

	
	/**============================ RECEPTIONCI =============================*/
	/**
	 * @param m the message to accept 
	 * @throws Exeption
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionImplementationI)this.owner).acceptMessage(m);
	}

	/**
	 * @param ms a list of messages to accept
	 * @throws Exeption
	 */
	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		((ReceptionImplementationI)this.owner).acceptMessages(ms);
	}

}