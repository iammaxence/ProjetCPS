package plugins;

import annexes.message.interfaces.MessageFilterI;
import connectors.ManagementConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import interfaces.ReceptionCI;
import interfaces.SubscriptionImplementationI;
import launcher.CVM;
import ports.ManagementCOutBoundPort;
import ports.ReceptionCInBoundPort;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class SubscriberPlugin 
extends AbstractPlugin implements SubscriptionImplementationI{

	private static final long serialVersionUID = 1L;
	protected ManagementCOutBoundPort managementOutboundPort;
	protected ReceptionCInBoundPort recepetionInboundPort;
	
	
	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/
	
	@Override
	public void installOn(ComponentI owner) throws Exception {
		super.installOn(owner) ;
		/**----------------- ADD COMPONENTS -------------------*/
		this.addRequiredInterface(ManagementCI.class);
		this.addOfferedInterface(ReceptionCI.class);
		
		/**---------------- PORTS CREATION --------------------*/
		this.managementOutboundPort=new ManagementCOutBoundPort(this.owner);
		this.recepetionInboundPort=new ReceptionCInBoundPort(this.owner);
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.managementOutboundPort.publishPort(); 
		this.recepetionInboundPort.publishPort();

	}
	
	@Override
	public void initialise() throws Exception {
		// Use the reflection approach to get the URI of the inbound port

		this.addRequiredInterface(ReflectionI.class) ;
		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner) ;
		rop.publishPort() ;
		
		this.owner.doPortConnection(
				rop.getPortURI(),
				CVM.BROKER_COMPONENT_URI,
				ReflectionConnector.class.getCanonicalName()) ;
		
		String[] uris = rop.findPortURIsFromInterface(ManagementCI.class) ;
		
		assert	uris != null && uris.length == 1 ;

		this.owner.doPortDisconnection(rop.getPortURI()) ;
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;

		// connect the outbound port.
		this.owner.doPortConnection(
				this.managementOutboundPort.getPortURI(),
				uris[0],
				ManagementConnector.class.getCanonicalName()) ;

		super.initialise();
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
		
		this.removeRequiredInterface(PublicationCI.class) ;
		this.removeRequiredInterface(ReceptionCI.class) ;
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
	
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topic, inboundPortUri);
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortUri);
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topic, filter, inboundPortUri);
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.managementOutboundPort.modifyFilter(topic, newFilter, inboundPortUri);
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortURI);
	}

}
