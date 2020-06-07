package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import plugins.SubscriberPlugin;
import tests.EcritureCSV;
import annexes.Chrono;
import annexes.message.Properties;
import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;

/**
 * The class <code> Subscriber </code> represent 
 * a subscriber that will subscribe in specific 
 * topics and that will receive a message if there
 * is a publication in theses topics.
 * 
 * @author GROUP LAMA
 *
 */

@RequiredInterfaces(required = {ManagementCI.class})
@OfferedInterfaces(offered = {ReceptionCI.class} )
public class Subscriber 
extends AbstractComponent implements ReceptionImplementationI{
	
	
	/**-------------------- Variables ---------------------*/
	protected final String                       uri;
	protected String                       receptionInboundPortURI ;
	protected SubscriberPlugin                   myplugin;
	protected final static String mypluginURI = "subscriberPlugin";
	
	/**
	 * Constructor of Subscriber Component
	 * 
	 * @pre uri != null
	 * @pre URI_BROKER != null
	 * 
	 * @param nbThreads is the number of thread s
	 * @param nbSchedulableThreads is the number of schedulable threads
	 * @param uri of the Component
	 * @param URI_BROKER uri of the broker in the same jvm
	 * @throws Exception
	 */
	protected Subscriber(int nbThreads, int nbSchedulableThreads, String uri, String URI_BROKER) throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		
		assert uri != null;
		assert URI_BROKER != null;
		
		this.uri = uri;
		
		/**----------- PLUGIN INSTALLATION ------------**/
		myplugin = new SubscriberPlugin(URI_BROKER);
		myplugin.setPluginURI(mypluginURI);


		/**----------------- TRACE --------------------**/
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(0, 1) ;
		this.toggleTracing() ;
	}
	
	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Subscriber component.") ;
		
		assert	this.isStarted();
		
		/**----------- PLUGIN INSTALLATION ------------**/
		try {
			this.installPlugin(myplugin);
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
		assert this.isInstalled(mypluginURI);
		this.receptionInboundPortURI = myplugin.getReceptionURI();
		assert receptionInboundPortURI != null;
		
		/**--------------Scenarios for the DCVM  ----------------------**/
		if (AbstractCVM.isDistributed) {
			try {	
				
				/**
				 * Choose scenario that you want (1 to 4):
				 */
				int[] scenario = {1,2};

				Chrono chrono=new Chrono(); //Chrono donne la preuve que les thread améliore le temps de calcul
				chrono.start();

				for(int i=0; i<scenario.length; i++) {
					switch (scenario[i]) {
					case 1: /** Scenario 1: Subscribe to the topic "Peche&Cuisine". We use the filter "thon" **/
						this.logMessage("Subscriber subcribe to the topic Peche&Cuisine.");
						MessageFilterI filter = m -> {Properties props = m.getProperties(); 
						try {
							if(!props.getBooleanProp("thon")) //Don't want a message with the word "thon" inside
								return true;
							return false;
						} catch (Exception e) { e.printStackTrace(); return false; }} ;
						myplugin.subscribe("Peche&Cuisine", filter ,receptionInboundPortURI);
						myplugin.subscribe("Automobile", receptionInboundPortURI);
						break;

					case 2: /**------Scenarios 2: Suscribe to the topic Automobile **/
						if (this.uri.equals("my-URI-Subscriber2")) {
							myplugin.subscribe("Automobile", receptionInboundPortURI); 
							this.logMessage("Subscriber subcribe to Test");
						}

					default: break;	
					}
				}

				chrono.stop();
				this.logMessage("Chrono sub : "+chrono.getDureeMs()); 
			} catch (Exception e) {
				throw new RuntimeException(e) ;
			}
		}
		/**------------------------------ End of Scenarios DCVM -----------------------------**/
		else {
			/**------------------- Scenarios for the CVM ---------------**/
			try {	
				String [] topics = {"Automobile", "Voyage","Sport","Nature&Decouvre"};   //Pour que le Subscriber soit abonné aux topics avant la publication

				/**
				 * Choose scenario that you want (1 to 4):
				 */
				int[] scenario = {1, 2, 4, 5};

				Chrono chrono=new Chrono(); //Chrono permet la preuve que les thread améliore le temps de calcul
				chrono.start();

				for(int i=0; i<scenario.length; i++) {
					switch (scenario[i]) {
					case 1: /** Scenario 1: Subscribe to the topic "Peche&Cuisine". We use the filter "thon" **/
						this.logMessage("Subscriber subcribe to the topic Peche&Cuisine.");
						MessageFilterI filter = m -> {Properties props = m.getProperties(); 
						try {
							if(!props.getBooleanProp("thon")) //Don't want a message with the word "thon" inside
								return true;
							return false;
						} catch (Exception e) { e.printStackTrace(); return false; }} ;
						myplugin.subscribe("Peche&Cuisine", filter ,receptionInboundPortURI);
						break;

					case 2: /** Scenario 2: Subscribe to a list of topics**/
						myplugin.subscribe(topics, receptionInboundPortURI);
						this.logMessage("Subscriber subcribe to Automobile, Voyage, Sport and Nature&Decouvre");
						break;

					case 3: /** Scenario 3: Subscribe to 100 topics**/
						for(int y=0; y<100; y++) {
							myplugin.subscribe("topic"+y, receptionInboundPortURI);
							//this.logMessage("Subscriber subcribe to Topic"+y);
						}
						break;
					case 4: /** Le composant subsciber1 s'abonne à Test **/
						if (this.uri.equals("my-URI-subscribe1")) {
							myplugin.subscribe("Test", receptionInboundPortURI); 
							this.logMessage("Subscriber subcribe to Test");
						}

						break;
					case 5:
						if (this.uri.equals("my-URI-subscribe1")) {
							myplugin.subscribe("Automobile", receptionInboundPortURI); 
							this.logMessage("Subscriber subcribe to Test");
						}

					default: break;	
					}
				}

				chrono.stop();
				this.logMessage("Chrono sub : "+chrono.getDureeMs()); 
			} catch (Exception e) {
				throw new RuntimeException(e) ;
			}
			/**------------------------------ End of Scenarios -----------------------------**/
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

		super.finalise();
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
		this.logMessage(this.uri+" a reçu : "+ m.getPayload());
		EcritureCSV.Calculdutemps("Subscriber", "reception", System.currentTimeMillis());
		
	}

	/**
	 * @param ms a list of messages to accept
	 * @throws Exeption
	 */
	@Override
	public void acceptMessages(MessageI[] ms) throws Exception {
		for(MessageI m : ms) {
			this.acceptMessage(m);
		}
	}

}
