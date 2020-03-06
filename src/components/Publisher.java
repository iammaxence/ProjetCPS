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


@RequiredInterfaces(required = {ManagementCI.class, PublicationCI.class})
public class Publisher 
extends AbstractComponent implements ManagementImplementationI, PublicationsImplementationI {
	
	protected final static String mypluginURI="publisherPlugin";
	protected PublisherPlugin myplugin;
	
	protected Publisher(int nbThreads, int nbSchedulableThreads, String uri) throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		assert uri != null;
		
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
		
		
		MessageI[] messages = {new Message("Mon 1er message."), new Message("Mon second message.")};
		String [] topics = {"Peche&Cuisine", "CPS"};
		
		try {
			//Chrono permet la preuve que les thread améliore le temps de calcul
			Chrono chrono=new Chrono();
			chrono.start();
			
			scenario1();
			scenario2(topics);
			scenario3(messages);
			scenario4(messages, topics);
			scenario5();
			
			chrono.stop();
			this.logMessage("Chrono : "+chrono.getDureeMs()); 
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
		
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
	 * @param topic : The topic that will be created
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void createTopic(String topic) throws Exception {
		myplugin.createTopic(topic);
	}
	
	/**
	 * @param listTopics : Topics that will be created
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void createTopics(String[] topics) throws Exception {
		myplugin.createTopics(topics);
	}
	
	/**
	 * @param topic : The topic that will be destroy
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		myplugin.destroyTopic(topic);
	}
	
	/**
	 * @param topic : The topic that we are looking for
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return myplugin.isTopic(topic);
	}
	
	/**
	 * @return A list of topics that already exist
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
	 * @param m : The message to send
	 * @param topic The topic that will contain the message
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		myplugin.publish(m, topic);
	}
	
	
	/**
	 * 
	 * @param m : The message to send
	 * @param listTopics The list of topic that will contain the message m
	 * @return void
	 * @throws Exception 
	 */

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		myplugin.publish(m, topics);
	}
	
	
	/**
	 * @param ms : Messages to send
	 * @param topic The topic that will contain the message
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		myplugin.publish(ms, topic);
	}
	
	/**
	 * 
	 * @param ms : Messages to send
	 * @param topic : The topic that will contain the message
	 * @return void
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		myplugin.publish(ms, topics);
	}
	
	/**======================================================================================
	 * ================================== SCENARIO =====================================
	 ======================================================================================*/
	
	/**
	 * Publish 10 000 messages
	 * @throws Exception
	 */
	public void scenario1() throws Exception {
		
		this.logMessage("Publisher publit des messages dans Peche&Cuisine");
		for(int i=0; i<10000;i++) {
			this.publish( new Message("Le saumon c'est trop bon!"), "Peche&Cuisine");
		}
	}
	
	/**
	 * Publish a message in Peche&Cuisine and CPS
	 * @param topics
	 * @throws Exception
	 */
	public void scenario2(String[] topics) throws Exception {
		this.logMessage("Publisher publit un message dans Peche&Cuisine et CPS");
		this.publish(new Message("Hello World!"), topics);
	}
	
	/**
	 * Create a Topic and  publish a message in it
	 * @param messages
	 * @throws Exception
	 */
	public void scenario3(MessageI[] messages) throws Exception {
		this.logMessage("Publisher creer le topic Automobile");
		this.createTopic("Automobile");
		this.logMessage("Publisher publit des messages dans Automobile");
		this.publish(messages, "Automobile");
	}
	
	/**
	 * Publish lots of messages in diferents topics 
	 * @param messages
	 * @param topics
	 * @throws Exception
	 */
	public void scenario4(MessageI[] messages,String[] topics) throws Exception {
		this.logMessage("Publisher publit des messages Peche&Cuisine et CPS");
		this.publish(messages, topics);
	}
	
	/**
	 * Publish a message in "Peche&Cuisine" with the filter "thon"
	 * @throws Exception
	 */
	public void scenario5() throws Exception {
		this.logMessage("Publisher publit des messages dans Peche&Cuisine");
		Properties props = new Properties();
		props.putProp("thon", true);
		Message msg = new Message("Non! Le thon c'est meilleur!", new TimeStamp(), props );
		this.publish(msg, "Peche&Cuisine");
	}
	
	

}
