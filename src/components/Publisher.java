package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import plugins.PublisherPlugin;
import annexes.Chrono;
import annexes.message.Message;
import annexes.message.Properties;
import annexes.message.TimeStamp;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
@RequiredInterfaces(required = {ManagementCI.class, PublicationCI.class})
public class Publisher 
extends AbstractComponent implements ManagementImplementationI, PublicationsImplementationI {
	
	protected final static String mypluginURI="publisherPlugin";
	protected PublisherPlugin myplugin;
	
	/**
	 * Constructor of Publisher Component
	 * @param nbThreads is the number of thread s
	 * @param nbSchedulableThreads is the number of schedulable threads
	 * @param uri of the Component
	 * @throws Exception
	 */
	protected Publisher(int nbThreads, int nbSchedulableThreads, String uri) throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		assert uri != null;
		
		/**----------- PLUGIN INSTALLATION ------------**/
		myplugin=new PublisherPlugin();
		myplugin.setPluginURI(mypluginURI);
		this.installPlugin(myplugin);
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
		
		/**------------------- Scenarios in order to tests of the methods ---------------**/
		try {
			MessageI[] messages = {new Message("Mon 1er message."), new Message("Mon second message.")};
			String [] topics = {"Peche&Cuisine", "CPS"};
			
			/**
			 * Choose scenario that you want (1 to 5):
			 */
			int[] scenario = {2, 3, 4, 5};
			
			Chrono chrono=new Chrono(); //Chrono permet la preuve que les thread améliore le temps de calcul
			chrono.start();
			for(int i=0; i<scenario.length; i++) {
				switch (scenario[i]) {
					case 1: /** Scenario 1: Publication of 10 000 in the topic Peche&Cuisine**/
						this.logMessage("Publisher publit des messages dans Peche&Cuisine");
						for(int y=0; y<10000; y++) {
							this.publish( new Message("Le saumon c'est trop bon!"), "Peche&Cuisine"); }
						break;
						
					case 2: /** Scenario 2: Publish a message in 2 topics **/
						this.logMessage("Publisher publit un message dans Peche&Cuisine et CPS");
						this.publish(new Message("Hello World!"), topics);
						break;
						
					case 3: /** Scenario 3: Create the topic Automobile and publish messages **/
						this.logMessage("Publisher creer le topic Automobile");
						this.createTopic("Automobile");
						this.logMessage("Publisher publit des messages dans Automobile");
						this.publish(messages, "Automobile");
						break;
						
					case 4: /** Scenario 4: publish messages in  Peche&Cuisine and CPS  **/
						this.logMessage("Publisher publit des messages Peche&Cuisine et CPS");
						this.publish(messages, topics);
						break;
						
					case 5: /** Scenario 5: Publish a message with properties in  Peche&Cuisine **/
						this.logMessage("Publisher publit des messages dans Peche&Cuisine");
						Properties props = new Properties();
						props.putProp("thon", true);
						Message msg = new Message("Non! Le thon c'est meilleur!", new TimeStamp(), props );
						this.publish(msg, "Peche&Cuisine");
						break;
						
					default: break;	
				}
			}
			chrono.stop();
			this.logMessage("Chrono : "+chrono.getDureeMs()); 
		} catch (Exception e) { throw new RuntimeException(e) ; }
		/**------------------------------ End of Scenarios -----------------------------**/
		
	}
	
	@Override
	public void	finalise() throws Exception{
		this.logMessage("stopping publisher component.") ;
		this.printExecutionLogOnFile("publisher");
		
		super.finalise();
	}
	
	
	/**======================================================================================
	 * =================================== MANAGEMENTCI =====================================
	 ======================================================================================*/
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that will be created
	 * @throws Exception 
	 */
	@Override
	public void createTopic(String topic) throws Exception {
		myplugin.createTopic(topic);
	}
	
	/**
	 * Method of ManagementCI
	 * @param listTopics : Topics that will be created
	 * @throws Exception 
	 */
	@Override
	public void createTopics(String[] topics) throws Exception {
		myplugin.createTopics(topics);
	}
	
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that will be destroy
	 * @throws Exception 
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		myplugin.destroyTopic(topic);
	}
	
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that we are looking for
	 * @return if the topic exist
	 * @throws Exception 
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return myplugin.isTopic(topic);
	}
	
	/**
	 * Method of ManagementCI
	 * @return The list of topics that already exist
	 * @throws Exception 
	 */
	@Override
	public String[] getTopics() throws Exception {
		return myplugin.getTopics();
	}


	/**======================================================================================
	 * ================================== PUBLICATIONCI =====================================
	 ======================================================================================*/
	/**
	 * Method of PublicationCI: publish a message for a topic
	 * @param m : The message to send
	 * @param topic : The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		myplugin.publish(m, topic);
	}
	
	
	/**
	 * Method of PublicationCI: publish a message for a list of topics
	 * @param m : The message to send
	 * @param listTopics : The list of topic that will contain the message m
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		myplugin.publish(m, topics);
	}
	
	
	/**
	 * Method of PublicationCI: publish messages for a topic
	 * @param ms : Messages to send
	 * @param topic The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		myplugin.publish(ms, topic);
	}
	
	/**
	 * Method of PublicationCI: publish messages for a list of topics
	 * @param ms : Messages to send
	 * @param topic The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		myplugin.publish(ms, topics);
	}

}
