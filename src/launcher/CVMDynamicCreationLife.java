package launcher;

import components.DynamicAssembler;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class CVMDynamicCreationLife extends AbstractCVM {
	public static final String BROKER_COMPONENT_URI = "my-URI-Broker" ;
	public static final String PUBLISHER_COMPONENT_URI = "my-URI-Publisher" ;
	public static final String SUBSCRIBE_COMPONENT_URI = "my-URI-Subscribe" ;


	/**
	 * Constructor CVM
	 */
	public CVMDynamicCreationLife() throws Exception {
		super() ;
	}

	
	/**
	 * instantiate the components, publish their port and interconnect them.
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void deploy() throws Exception {
		assert	!this.deploymentDone() ;
		
		/**=============================== DYNAMIC COMPONENT ====================================*/
		AbstractComponent.createComponent(
				DynamicAssembler.class.getCanonicalName(),
				new Object[]{AbstractCVM.thisJVMURI,
						1,
						0}) ;
		
		/**------------------------------------ Deployment Done --------------------------------*/
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
		super.shutdown();
	}

	
	/**
	 * ********** MAIN *************
	 */
	public static void	main(String[] args){
		try {
			// Create an instance of the defined component virtual machine.
			CVMDynamicCreationLife a = new CVMDynamicCreationLife() ;
			// Execute the application.
			a.startStandardLifeCycle(20000L) ;
			Thread.sleep(5000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
