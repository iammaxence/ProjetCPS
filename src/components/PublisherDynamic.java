package components;

import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.pre.dcc.DynamicComponentCreator;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import annexes.message.interfaces.MessageI;
import ports.ManagementCOutBoundPort;
import ports.PublicationCOutBoundPort;


@RequiredInterfaces(required = {ManagementCI.class, PublicationCI.class})
public class PublisherDynamic
extends DynamicComponentCreator implements ManagementImplementationI, PublicationsImplementationI {

	/**------------------- PORTS -------------------------*/
	protected ManagementCOutBoundPort     managementOutboundPort;
	protected PublicationCOutBoundPort    publicationOutboundPort;
	
	
	protected PublisherDynamic(String dynamicComponentCreationInboundPortURI,
								String managementOutboundPortURI,
								String publicationOutboundPortURI) throws Exception{
		super(dynamicComponentCreationInboundPortURI);
		
		assert dynamicComponentCreationInboundPortURI != null;
		assert managementOutboundPortURI != null;
		assert publicationOutboundPortURI != null;
		
		
		/**----------------- ADD COMPONENTS -------------------*/
		this.addRequiredInterface(ManagementCI.class);
		this.addRequiredInterface(PublicationCI.class);
		
		/**---------------- PORTS CREATION --------------------*/
		this.managementOutboundPort = new ManagementCOutBoundPort(managementOutboundPortURI,this);
		this.publicationOutboundPort = new PublicationCOutBoundPort(publicationOutboundPortURI, this);
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.managementOutboundPort.publishPort(); 
		this.publicationOutboundPort.publishPort();
	}
	
	/**-----------------------------------------------------
	 * -------------------- LIFE CYCLE ---------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Publisher component.") ;
	}
	
	@Override
	public void	execute() throws Exception{
		super.execute();
		
		
	}
	

	@Override
	public void	finalise() throws Exception{
		this.logMessage("stopping publisher component.") ;
		this.printExecutionLogOnFile("publisher");

		/**------------------ DELETE PORTS --------------------*/
		this.managementOutboundPort.unpublishPort(); 
		this.publicationOutboundPort.unpublishPort();
		
		super.finalise();
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

}
