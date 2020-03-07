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
		
		String [] topics = {"Automobile", "Voyage","Sport","Nature&Decouvre"};   //Pour que le Subscriber soit abonné aux topics avant la publication
		
		//Chrono permet la preuve que les thread améliore le temps de calcul
		Chrono chrono=new Chrono();
		chrono.start();
		
		try {	
			scenario1(topics);
			scenario2(topics);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		chrono.stop();
		this.logMessage("Chrono sub : "+chrono.getDureeMs()); 
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
	 * @param topic where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		myplugin.subscribe(topic, inboundPortUri);
	}

	/**
	 * @param listTopics where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		myplugin.subscribe(topics, inboundPortUri);
	}

	/**
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
		this.logMessage(this.uri+" a reçu le message "+ m.getPayload());
		
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
	
	/**======================================================================================
	 * =================================== SCENARIO =====================================
	 ======================================================================================
	  */
	
	/**
	 * Subscribe to the topic "Peche&Cuisine". We use the filter "thon"
	 * @throws Exception
	 */
	
	public void scenario1(String[] topics) throws Exception {
		
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
	}
	
	/**
	 * Subscribe to a list of topics
	 * @param topics
	 * @throws Exception
	 */
	
	public void scenario2(String[] topics) throws Exception {
	
		this.subscribe(topics, receptionInboundPortURI);
		this.logMessage("Subscriber subcribe to Automobile");
	}
	
	/**
	 * Subscribe to 100 topics
	 * @throws Exception
	 */
	
	public void scenario3() throws Exception {
		for(int i=0;i<100;i++) {
			this.subscribe("topic"+i, receptionInboundPortURI);
		}
	}
	

}
