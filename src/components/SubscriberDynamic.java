package components;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.pre.dcc.DynamicComponentCreator;
import interfaces.ManagementCI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import interfaces.SubscriptionImplementationI;
import annexes.message.Properties;
import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;
import ports.ManagementCOutBoundPort;
import ports.ReceptionCInBoundPort;

@RequiredInterfaces(required = {ManagementCI.class})
@OfferedInterfaces(offered = {ReceptionCI.class} )
public class SubscriberDynamic
extends DynamicComponentCreator implements ReceptionImplementationI, SubscriptionImplementationI {
	
	
	/**-------------------- PORTS -------------------------*/
	protected ReceptionCInBoundPort recepetionInboundPort;
	protected ManagementCOutBoundPort managementOutboundPort;
	
	/**-------------------- Variables ---------------------*/
	protected final String                dynamicComponentCreationInboundPortURI;
	protected final String                receptionInboundPortURI;

	protected SubscriberDynamic(String dynamicComponentCreationInboundPortURI, 	
								String managementOutboundPortURI,
								String receptionInboundPortURI) throws Exception{
		super(dynamicComponentCreationInboundPortURI);
		
		assert dynamicComponentCreationInboundPortURI != null;
		assert receptionInboundPortURI != null;
		assert managementOutboundPortURI != null;
		
		this.dynamicComponentCreationInboundPortURI = dynamicComponentCreationInboundPortURI;
		this.receptionInboundPortURI = receptionInboundPortURI;
		
		/**----------------- ADD COMPONENTS -------------------*/
		this.addRequiredInterface(ManagementCI.class);
		this.addOfferedInterface(ReceptionCI.class);
		
		/**---------------- PORTS CREATION --------------------*/
		this.managementOutboundPort=new ManagementCOutBoundPort(managementOutboundPortURI,this);
		this.recepetionInboundPort=new ReceptionCInBoundPort(receptionInboundPortURI, this);
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.managementOutboundPort.publishPort(); 
		this.recepetionInboundPort.publishPort();
	
		
	}
	
	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Subscriber component.") ;
		
		String [] topics = {"Automobile", "Voyage"};   //Pour que le Subscriber soit abonné aux topics avant la publication
		try {	
			//Scenario 1 : voir URIPublisher
			this.logMessage("Subscriber subcribe to the topic Peche&Cuisine.");
			MessageFilterI filter = m -> 
							{Properties props = m.getProperties(); 
							try {
								return props.getBooleanProp("thon");
							} catch (Exception e) {
								e.printStackTrace();
								return false;
							}} ;
			this.subscribe("Peche&Cuisine", filter ,receptionInboundPortURI);
			
			
			//Scenario 2 : Voir URIPublisher
			this.subscribe(topics, receptionInboundPortURI);
			this.logMessage("Subscriber subcribe to Automobile and Voyage.");
			
			
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	
	@Override
	public void	execute() throws Exception{
		super.execute();

	}
	
	@Override
	public void	finalise() throws Exception{
		this.logMessage("stopping subscriber component.") ;
		this.printExecutionLogOnFile("subscriber");
		
		/**------------------ DELETE PORTS --------------------*/
		this.managementOutboundPort.unpublishPort(); 
		this.recepetionInboundPort.unpublishPort();

		super.finalise();
	}
	
	
	/**======================================================================================
	 * =================================== MANAGEMENTCI =====================================
	 ======================================================================================*/
	/**
	 * @param topic where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topic, inboundPortUri);
	}

	/**
	 * @param listTopics where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topics, inboundPortUri);
	}

	/**
	 * @param topic where the subscriber want to subscribe
	 * @param filter of the topic
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception {
		this.managementOutboundPort.subscribe(topic, filter,inboundPortUri);
	}

	/**
	 * @param topic where the subscriber want to modify his filter
	 * @param newFilter is the new filter 
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.managementOutboundPort.modifyFilter(topic, newFilter, inboundPortUri);
	}

	/**
	 * @param topic where the subscriber want to unsubscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortURI);
	}

	
	/**=====================================================================================
	 * =================================== RECEPTIONCI =====================================
	 ======================================================================================*/
	/**
	 * @param m the message to accept 
	 * @throws Exeption
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.logMessage(this.dynamicComponentCreationInboundPortURI+" a reçu le message "+ m.getPayload());
	}

	/**
	 * @param ms a list of message 
	 * @throws Exeption
	 */
	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		for(MessageI m : ms) {
			this.acceptMessage(m);
		}
	}

}
