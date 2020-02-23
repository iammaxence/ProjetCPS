package launcher;

import components.Broker;
import components.Publisher;
import components.Subscriber;
import connectors.ManagementConnector;
import connectors.PublicationConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

public class CVM extends AbstractCVM {

	/**======================================================================================
	 * =================================== BROKER ===========================================
	 =======================================================================================*/
	protected String uriBrokerURI;
	protected static final String BROKER_COMPONENT_URI = "my-URI-Broker" ;
	protected static final String URIBrokerManagementInboundPortURI1 = "bm1-iport"; //For Publisher
	public static final String    URIBrokerManagementInboundPortURI2 = "bm2-iport"; //For Subscriber
	public static final String    URIBrokerPublicationInboundPortURI = "bp-iport";
	//protected static final String URIBrokerReceptionOutboundPortURI = "br-oport"; 

	
	
	/**=======================================================================================
	 * ==================================== PUBLISHER ========================================
	 ========================================================================================*/
	protected String uriPublisherURI;
	protected static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher" ;
	protected static final String URIPublisherManagementOutboundPortURI = "pm-iport";
	protected static final String URIPublisherPublicationOutboundPortURI = "pp-iport";
	
	
	/**========================================================================================
	 * ===================================== SUBSCRIBER =======================================
	 =========================================================================================*/
	protected String uriSubscriberURI;
	protected static final String SUBSCRIBE_COMPONENT_URI = "my-URI-subscribe" ;
	protected static final String URISubscriberRecepetionInboundPortURI = "sr-iport"; 
	protected static final String URISubscriberManagementOutboundPortURI = "sm-iport"; 
	
	
	/**
	 * Constructor CVM
	 */
	public CVM() throws Exception {
		super() ;
	}

	
	/**
	 * instantiate the components, publish their port and interconnect them.
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		assert	!this.deploymentDone() ;

		/**-------------------------------------------------------------------------------------
		 * ------------------------------- Configuration phase ---------------------------------
		 --------------------------------------------------------------------------------------*/

		// debugging mode configuration; 
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.) ;
//		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.COMPONENT_DEPLOYMENT) ;

		/**----------------------------------- Create Phase -------------------------------------
		 ---------------------------------------------------------------------------------------*/

		/**======================================================================================
		 * =============================== BROKER COMPONENT =====================================
		 ======================================================================================*/
		this.uriBrokerURI =
			AbstractComponent.createComponent(
					Broker.class.getCanonicalName(),
					new Object[]{1,
								 0,
								 BROKER_COMPONENT_URI,
								 URIBrokerManagementInboundPortURI1,
								 URIBrokerManagementInboundPortURI2,
								 URIBrokerPublicationInboundPortURI}) ; 
		
		assert	this.isDeployedComponent(this.uriBrokerURI) ;
		this.toggleTracing(this.uriBrokerURI) ;
		this.toggleLogging(this.uriBrokerURI) ;

		/**========================================================================================
		 * ============================== PUBLISHER COMPONENT =====================================
		 ========================================================================================*/
		this.uriPublisherURI =
			AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{1,
								0,
								PUBLISHER_COMPONENT_URI,
								URIPublisherManagementOutboundPortURI,
								URIPublisherPublicationOutboundPortURI}) ;
		
		assert	this.isDeployedComponent(this.uriPublisherURI) ;
		this.toggleTracing(this.uriPublisherURI) ;
		this.toggleLogging(this.uriPublisherURI) ;
		
		/**========================================================================================
		 * ================================ SUBSCRIBER COMPONENT ==================================
		 ==========================================================================================*/
		this.uriSubscriberURI =
			AbstractComponent.createComponent(
					Subscriber.class.getCanonicalName(),
					new Object[]{1,
								0,
								SUBSCRIBE_COMPONENT_URI,
								URISubscriberManagementOutboundPortURI,
								URISubscriberRecepetionInboundPortURI}) ;
		
		assert	this.isDeployedComponent(this.uriSubscriberURI) ;
		this.toggleTracing(this.uriSubscriberURI) ;
		this.toggleLogging(this.uriSubscriberURI) ;
		

		/**---------------------------------------------------------------------------------------
		 * ------------------------------------ Connection Phase ---------------------------------
		 ---------------------------------------------------------------------------------------*/

		//publisher -> broker (Management)
		this.doPortConnection(
				this.uriPublisherURI,
				URIPublisherManagementOutboundPortURI,
				URIBrokerManagementInboundPortURI1,
				ManagementConnector.class.getCanonicalName()) ;
		
		//publisher -> broker (Publication)
		this.doPortConnection(
				this.uriPublisherURI,
				URIPublisherPublicationOutboundPortURI,
				URIBrokerPublicationInboundPortURI,
				PublicationConnector.class.getCanonicalName()) ;
		
		//subscriber -> broker( Management)
		this.doPortConnection(
				this.uriSubscriberURI,
				URISubscriberManagementOutboundPortURI,
				URIBrokerManagementInboundPortURI2,
				ManagementConnector.class.getCanonicalName()) ;
		
//		//broker -> subscriber( Reception)
//		this.doPortConnection(
//				this.uriBrokerURI,
//				URIBrokerReceptionOutboundPortURI,
//				URISubscriberRecepetionInboundPortURI,
//				ReceptionConnector.class.getCanonicalName()) ;
		
		
		/**---------------------------------------------------------------------------------------
		 * ------------------------------------ Deployment Done ----------------------------------
		 ---------------------------------------------------------------------------------------*/
		
		super.deploy();
		assert	this.deploymentDone() ;
		
	}

	
	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#finalise()
	 */
	@Override
	public void	finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		
		this.doPortDisconnection(
				this.uriPublisherURI,
				URIPublisherManagementOutboundPortURI) ;
		this.doPortDisconnection(
				this.uriPublisherURI,
				URIPublisherPublicationOutboundPortURI) ;
		this.doPortDisconnection(
				this.uriSubscriberURI,
				URISubscriberManagementOutboundPortURI) ;
//		this.doPortDisconnection(
//				this.uriBrokerURI,
//				URIBrokerReceptionOutboundPortURI) ;

		super.finalise();
	}

	
	/**
	 * disconnect the components and then call the base shutdown method.
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#shutdown()
	 */
	@Override
	public void shutdown() throws Exception {
		assert	this.allFinalised() ;
		// any disconnection not done yet can be performed here

		super.shutdown();
	}

	
	/**
	 * ********** MAIN *************
	 */
	public static void	main(String[] args){
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM() ;
			// Execute the application.
			a.startStandardLifeCycle(20000L) ;
			// Give some time to see the traces (convenience).
			Thread.sleep(5000L) ;
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
