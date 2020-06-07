package launcher;

import components.Broker;
import components.Publisher;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * Class CVMTestMultiClients executable
 * with a unique Broker, multiple Publisher
 * and Subscriber
 * 
 * 
 * @author GROUP LAMA
 *
 */
public class CVMTestMultiClients extends AbstractCVM {

	/**=================================== BROKER ==========================================*/
	protected String brokerURI;
	public static final String BROKER_COMPONENT_URI = "my-URI-Broker" ;
	
	
	/** ==================================== PUBLISHER ======================================*/
	protected String publisherURI;
	protected static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher" ;

	
	/** ===================================== SUBSCRIBER ======================================*/
	protected String subscriberURI;
	protected static final String SUBSCRIBE_COMPONENT_URI = "my-URI-subscribe" ;


	
	/**
	 * Constructor CVMTest 
	 * Test le multi-composant
	 */
	public CVMTestMultiClients() throws Exception {
		super() ;
	}

	
	/**
	 * instantiate the components, publish their port and interconnect them.
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		assert	!this.deploymentDone() ;


		/**----------------------------------- Create Phase -------------------------------------
		 ---------------------------------------------------------------------------------------*/
		int nb_publisher = 3;
		int nb_subscriber = 4;
		
		/**=============================== BROKER COMPONENT =================================*/
		this.brokerURI =
			AbstractComponent.createComponent(
					Broker.class.getCanonicalName(),
					new Object[]{1,
								 0,
								 BROKER_COMPONENT_URI}) ; 
		
		assert	this.isDeployedComponent(this.brokerURI);
		

		/**============================== PUBLISHERS COMPONENT ==================================*/
		
		//Creation de 3 publishers de nom : my-URI-publisheri ( i compris entre 0 et 2 inclus)
		for (int i=0; i< nb_publisher; i++) {
			this.publisherURI = AbstractComponent.createComponent(
					Publisher.class.getCanonicalName(),
					new Object[]{1,
							0,
							PUBLISHER_COMPONENT_URI+i,
							BROKER_COMPONENT_URI}) ;

			assert	this.isDeployedComponent(this.publisherURI) ;

		}
		/**================================ SUBSCRIBERS COMPONENT =================================*/
		
		//Creation de 4 subscribers de nom : my-URI-subscribei ( i compris entre 0 et 3 inclus)
		for(int i=0; i< nb_subscriber; i++) {
			this.subscriberURI =
				AbstractComponent.createComponent(
						Subscriber.class.getCanonicalName(),
						new Object[]{1,
									0,
									SUBSCRIBE_COMPONENT_URI+i,
									BROKER_COMPONENT_URI}) ;
			
			assert	this.isDeployedComponent(this.subscriberURI) ;
		}
		
	
		
		
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
	public void	finalise() throws Exception {		
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
			CVMTestMultiClients a = new CVMTestMultiClients() ;
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
