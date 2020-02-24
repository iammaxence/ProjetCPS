package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import interfaces.SubscriptionImplementationI;
import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;
import ports.ManagementCOutBoundPort;
import ports.ReceptionCInBoundPort;

@RequiredInterfaces(required = {ManagementCI.class})
@OfferedInterfaces(offered = {ReceptionCI.class} )
public class Subscriber 
extends AbstractComponent implements ReceptionImplementationI, SubscriptionImplementationI {
	
	
	/**-------------------- PORTS -------------------------*/
	protected ReceptionCInBoundPort recepetionInboundPort;
	protected ManagementCOutBoundPort managementOutboundPort;
	
	/**-------------------- Variables ---------------------*/
	protected final String                uri;
	protected final String                receptionInboundPortURI;

	protected Subscriber(int nbThreads, int nbSchedulableThreads,
							String uri, 	
							String managementOutboundPortURI,
							String receptionInboundPortURI) throws Exception{
		super(nbThreads, nbSchedulableThreads);
		
		assert uri != null;
		assert receptionInboundPortURI != null;
		assert managementOutboundPortURI != null;
		
		this.uri = uri;
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
	}
	
	@Override
	public void	execute() throws Exception{
		super.execute();
		String [] topics = {"Automobile", "CPS"};
		
		try {	
			//Scenario 1 : voir URIPublisher
			this.logMessage("Subscriber subcribe to a topic.");
			this.subscribe("Peche&Cuisine", receptionInboundPortURI);
			
			
			//Scenario 2 : Voir URIPublisher
			this.subscribe(topics, receptionInboundPortURI);
			this.logMessage("Subscriber subcribe to 2 topic.");
			
			
			this.logMessage("END OF SUBSCRIBE");
			
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
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
		this.managementOutboundPort.subscribe(topic, filter,inboundPortUri);
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.managementOutboundPort.modifyFilter(topic, newFilter, inboundPortUri);
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.managementOutboundPort.unsubscribe(topic, inboundPortURI);
	}

	
	/**=====================================================================================
	 * =================================== RECEPTIONCI =====================================
	 ======================================================================================*/
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.logMessage(this.uri+" a reçu le message "+ m.getPayload());
	}

	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		for(MessageI m : ms) {
			this.acceptMessage(m);
		}
	}

}
