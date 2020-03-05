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
			
			//Scenario 1 : Publisher publie dans un Topic et un subsciber reçoit le message
			this.logMessage("Publisher publit des messages dans Peche&Cuisine");
			for(int i=0; i<10000;i++) {
				this.publish( new Message("Le saumon c'est trop bon!"), "Peche&Cuisine");
			}
			
			//Scenario 2: Publisher publie dans plusieurs Topic, seul les subs abonnées aux topics reçoivent les messages
			this.logMessage("Publisher publit un message dans Peche&Cuisine et CPS");
			this.publish(new Message("Hello World!"), topics);
			
			//Scenario 3: Publisher publie plusieurs messages dans un topic	creer a l'avance
			this.logMessage("Publisher creer le topic Automobile");
			this.createTopic("Automobile");
			this.logMessage("Publisher publit des messages dans Automobile");
			this.publish(messages, "Automobile");
			
			//Scenario 4: Publisher publie plusieurs messages dans plusieurs topics	
			this.logMessage("Publisher publit des messages Peche&Cuisine et CPS");
			this.publish(messages, topics);
			
			//Scenario 5: Publisher publie un message avec une propriété
			this.logMessage("Publisher publit des messages dans Peche&Cuisine");
			Properties props = new Properties();
			props.putProp("thon", true);
			Message msg = new Message("Non! Le thon c'est meilleur!", new TimeStamp(), props );
			this.publish(msg, "Peche&Cuisine");
			
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
	@Override
	public void createTopic(String topic) throws Exception {
		myplugin.createTopic(topic);
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		myplugin.createTopics(topics);
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		myplugin.destroyTopic(topic);
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return myplugin.isTopic(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		return myplugin.getTopics();
	}



	/**======================================================================================
	 * ================================== PUBLICATIONCI =====================================
	 ======================================================================================*/
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		myplugin.publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		myplugin.publish(m, topics);
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		myplugin.publish(ms, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		myplugin.publish(ms, topics);
	}

}
