package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.ReceptionCI;
import interfaces.ReceptionImplementationI;
import interfaces.SubscriptionImplementationI;
import plugins.SubscriberPlugin;
import annexes.Chrono;
import annexes.message.Properties;
import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
@RequiredInterfaces(required = {ManagementCI.class})
@OfferedInterfaces(offered = {ReceptionCI.class} )
public class Subscriber 
extends AbstractComponent implements ReceptionImplementationI, SubscriptionImplementationI {
	
	
	/**-------------------- Variables ---------------------*/
	protected final String                       uri;
	protected final String                       receptionInboundPortURI;
	protected SubscriberPlugin                   myplugin;
	protected final static String mypluginURI = "subscriberPlugin";
	
	protected Subscriber(int nbThreads, int nbSchedulableThreads, String uri) throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		
		assert uri != null;
		this.uri = uri;
		
		/**----------- PLUGIN INSTALLATION ------------**/
		myplugin = new SubscriberPlugin();
		myplugin.setPluginURI(mypluginURI);
		this.installPlugin(myplugin);

		this.receptionInboundPortURI = myplugin.getReceptionURI();
		assert receptionInboundPortURI!=null;
	}
	
	/**-----------------------------------------------------
	 * ------------------ CYCLE OF LIFE --------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Subscriber component.") ;
		
	
		/**------------------- Scenarios in order to tests of the methods ---------------**/
		try {	
			String [] topics = {"Automobile", "Voyage","Sport","Nature&Decouvre"};   //Pour que le Subscriber soit abonné aux topics avant la publication
			
			/**
			 * Choose scenario that you want (1 to 3):
			 */
			int[] scenario = {1, 2, 3};
			
			Chrono chrono=new Chrono(); //Chrono permet la preuve que les thread améliore le temps de calcul
			chrono.start();
			
			for(int i=0; i<scenario.length; i++) {
				switch (scenario[i]) {
					case 1: /** Scenario 1: Subscribe to the topic "Peche&Cuisine". We use the filter "thon" **/
						this.logMessage("Subscriber subcribe to the topic Peche&Cuisine.");
						MessageFilterI filter = m -> {Properties props = m.getProperties(); 
													try {
														if(props.contains("thon")) 
															return props.getBooleanProp("thon");
														else
															return false;
													} catch (Exception e) { e.printStackTrace(); return false; }} ;
						this.subscribe("Peche&Cuisine", filter ,receptionInboundPortURI);
						break;
						
					case 2: /** Scenario 2: Subscribe to a list of topics**/
						this.subscribe(topics, receptionInboundPortURI);
						this.logMessage("Subscriber subcribe to Automobile");
						break;
						
					case 3: /** Scenario 3: Subscribe to 100 topics**/
						for(int y=0;y<100;y++) {
							this.subscribe("topic"+y, receptionInboundPortURI);
						}
						break;
						
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
	
	
	/**======================================================================================
	 * =================================== MANAGEMENTCI =====================================
	 ======================================================================================*/
	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		myplugin.subscribe(topic, inboundPortUri);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param listTopics where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		myplugin.subscribe(topics, inboundPortUri);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param filter of the topic
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception {
		myplugin.subscribe(topic, filter,inboundPortUri);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to modify his filter
	 * @param newFilter is the new filter 
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		myplugin.modifyFilter(topic, newFilter, inboundPortUri);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to unsubscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		myplugin.unsubscribe(topic, inboundPortURI);
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
