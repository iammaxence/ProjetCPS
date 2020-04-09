package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.ManagementImplementationI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import interfaces.ReceptionCI;
import interfaces.SubscriptionImplementationI;
import annexes.message.interfaces.MessageFilterI;
import annexes.message.interfaces.MessageI;
import annexes.Client;
import annexes.TopicKeeper;
import ports.ManagementCInBoundPort;
import ports.PublicationCInBoundPort;
import ports.ReceptionCOutBoundPort;
import plugins.BrokerManagementDynamicPlugin;

/**
 * 
 * @author GROUP LAMA
 *
 */
@OfferedInterfaces(offered= {ManagementCI.class,PublicationCI.class})
@RequiredInterfaces(required = {ReceptionCI.class} )
@AddPlugin(pluginClass = BrokerManagementDynamicPlugin.class,
		   pluginURI = Broker.SUBS_DYNAMIC_CONNECTION_PLUGIN_URI)
public class Broker 
extends AbstractComponent 
implements ManagementImplementationI, SubscriptionImplementationI, PublicationsImplementationI{


	/**------------------- PORTS -------------------------*/
	protected ManagementCInBoundPort      mipPublisher;   // Connected to URIPublisher 
	protected ManagementCInBoundPort      mipSubscriber;  // Connected to URISubscriber 
	protected PublicationCInBoundPort     publicationInboundPort;
	
	
	/**------------------ VARIABLES ----------------------*/
	protected TopicKeeper                                     topics;          //<Topics, List of messages>
	protected ArrayList<Client>                               subscribers;     // List of Subscriber
	protected Map <String, ArrayList<Client> >                subscriptions;   //<Topics, List of Subscriber>
	private int cpt = 0;
	private int threadPublication, threadSubscription;
	public final static String	SUBS_DYNAMIC_CONNECTION_PLUGIN_URI = "brokerManagementPluginURI" ;
	
	/**----------------------- MUTEX ----------------------*/
	protected ReadWriteLock lock = new ReentrantReadWriteLock();
	protected Lock readLock = lock.readLock();
	protected Lock writeLock = lock.writeLock();
	
	
	
	/**
	 * Constructor of Broker Component
	 * @param nbThreads is the number of threads
	 * @param nbSchedulableThreads id the number of schedular threads
	 * @param uri of the component
	 * @param managInboundPortPublisherURI is the URI of the port connected to the Publisher (ManagementCI)
	 * @param managInboundPortSubscriberURI is the URI of the port connected to the Subscriber
	 * @param publicationInboundPortURI is the URI of the port connected to the Publisher (PublicationCI)
	 * @throws Exception
	 */
	protected Broker(int nbThreads, int nbSchedulableThreads, String uri) throws Exception {
		super(uri, nbThreads, nbSchedulableThreads);
		
		topics = new TopicKeeper();  
		subscriptions = new HashMap <String, ArrayList<Client>>();  
		subscribers = new ArrayList<Client>();
		
		/**---------------------- THREADS ---------------------*/
		threadPublication = createNewExecutorService("threadPublication", 10, false);
		threadSubscription = createNewExecutorService("threadSubscription", 10,false);
		
		/**----------------- ADD COMPONENTS -------------------*/
		this.addOfferedInterface(ManagementCI.class);
		this.addOfferedInterface(PublicationCI.class);
		this.addRequiredInterface(ReceptionCI.class);
		
		/**---------------- PORTS CREATION --------------------*/
		this.mipPublisher = new ManagementCInBoundPort(this);
		this.mipSubscriber = new ManagementCInBoundPort(this,threadSubscription);
		this.publicationInboundPort = new PublicationCInBoundPort(this, threadPublication);
		
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.mipPublisher.publishPort(); 
		this.mipSubscriber.publishPort(); 
		this.publicationInboundPort.publishPort();
		
		
		/**----------------- TRACE --------------------**/
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(0, 0) ;
		this.toggleTracing() ;	
	}
	
	
	/**-----------------------------------------------------
	 * -------------------- LIFE CYCLE ---------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Broker component.") ;
		
	}
	
	@Override
	public void	execute() throws Exception{
		super.execute();
		
	}
	

	@Override
	public void	finalise() throws Exception{
		this.logMessage("stopping broker component.") ;
		this.printExecutionLogOnFile("broker");
		
		/**------------------ DELETE PORTS --------------------*/
		this.mipPublisher.unpublishPort(); 
		this.mipSubscriber.unpublishPort(); 
		this.publicationInboundPort.unpublishPort();
		
		/**----------------------------------------------------*/
//		for(Client c : subscribers) {
//			this.doPortDisconnection(c.getOutBoundPortURI());
//		}
		
		super.finalise();
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
		topics.addMessage(topic, m);

		this.sendMessage(m, topic);
		this.logMessage("Broker: Message publié dans "+topic);
	}
	
	/**
	 * Method of PublicationCI: publish a message for a list of topics
	 * @param m : The message to send
	 * @param listTopics : The list of topic that will contain the message m
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI m, String[] listTopics) throws Exception {
		for(String topic: listTopics)
			publish(m, topic);
	}
	
	/**
	 * Method of PublicationCI: publish messages for a topic
	 * @param ms : Messages to send
	 * @param topic The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		topics.addMessages(topic, ms);
		this.sendMessages(ms, topic);
		this.logMessage("Broker: Message publié dans "+topic);
	}
	
	
	/**
	 * Method of PublicationCI: publish messages for a list of topics
	 * @param ms : Messages to send
	 * @param listTopics : The topic that will contain the message
	 * @throws Exception 
	 */
	@Override
	public void publish(MessageI[] ms, String[] listTopics) throws Exception {
		for(String topic: listTopics)
			publish(ms, topic);
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
		topics.createTopic(topic);
	}
	
	/**
	 * Method of ManagementCI
	 * @param listTopics : Topics that will be created
	 * @throws Exception 
	 */
	@Override
	public void createTopics(String[] listTopics) throws Exception {
		topics.createTopics(listTopics);
	}
	
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that will be destroy
	 * @throws Exception 
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		topics.removeTopic(topic);
		this.logMessage("Le topic << "+topic+" >> a été supprimé.");
	}
	
	/**
	 * Method of ManagementCI
	 * @param topic : The topic that we are looking for
	 * @return if the topic exist
	 * @throws Exception 
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return topics.isTopic(topic);
	}
	
	/**
	 * Method of ManagementCI
	 * @return The list of topics that already exist
	 * @throws Exception 
	 */
	@Override
	public String[] getTopics() throws Exception {
		return topics.getTopics();
	}


	/**------------------------------------------------------
	 * ----------------- (UN)SUBSCRIBE ----------------------
	 -------------------------------------------------------*/
	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.subscribe(topic, null, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param listTopics where the subscriber want to subscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String[] listTopics, String inboundPortURI) throws Exception {
		for(String t: listTopics)			//listTopics: local donc pas besoin de lock
			this.subscribe(t, null, inboundPortURI);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to subscribe
	 * @param filter of the topic
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		if(!isTopic(topic))
			this.createTopic(topic);

		if(!isSubscribe(topic, inboundPortURI)) {
			ArrayList<Client> subs;
			
			subs = getSubscriptions(topic); //Protegé par readLock   ! A VERIFER 
			
			Client monSub = getSubscriber(inboundPortURI); //Protegé par readLock
			
			if(monSub==null) { // Si le sub n'est pas dans la liste officiel des subscibers existants
				String portURI = "uri-"+cpt++;
				monSub = new Client(inboundPortURI, new ReceptionCOutBoundPort(portURI, this));  // Je crée le sub avec un port unique
				monSub.getPort().publishPort();
				this.doPortConnection(
						monSub.getOutBoundPortURI(),
						inboundPortURI, 
						ReceptionConnector.class.getCanonicalName()) ;
				
				writeLock.lock();
				subscribers.add(monSub); // J'ajoute le sub nouvellement crée dans la liste officiel des subscibers
				writeLock.unlock();
			}
			
			if(filter != null)
				monSub.setFilter(filter,topic); 
			
			// Je l'ajoute dans la liste de ceux abonnée au topic
			writeLock.lock();
			subs.add(monSub);
			subscriptions.put(topic, subs);
			writeLock.unlock();
			
			this.logMessage("Subscriber subscribe to topic "+topic);
		}else {
			this.logMessage("Subscriber already subscribe to topic "+topic);
		}


	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to modify his filter
	 * @param newFilter is the new filter 
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		writeLock.lock();
		for(Client s : subscriptions.get(topic)) {
			if(s.getInBoundPortURI().equals(inboundPortURI)) {
				s.setFilter(newFilter, topic);
				break;
			}
		}	
		writeLock.unlock();
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to unsubscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {

		Client trouve = null;
		readLock.lock();
		ArrayList<Client> clients = subscriptions.get(topic);
		for(Client s : clients) {
			if(s.getInBoundPortURI().equals(inboundPortURI)) {
				trouve = s;
				break;
			}
		}
		readLock.unlock();

		if(trouve != null) {
			this.logMessage("Unsubscribe of "+trouve.getOutBoundPortURI()+" for the topic "+topic);
			writeLock.lock();
			clients.remove(trouve);
			subscriptions.put(topic, clients);
			writeLock.unlock();
		}else {
			this.logMessage(inboundPortURI+" already unsubscribe");
		}

	}
	
	/**---------------------------------------------*/
	
	/**
	 * @param topic : the name of the topic
	 * @param inboundPortURI : the name of the inboundPortURI
	 * @return Check if the subscriber is already in the hashmap subs
	 * @throws Exception 
	 */
	private boolean isSubscribe(String topic, String inboundPortURI) throws Exception {
		try {
			readLock.lock();
			if(subscriptions.containsKey(topic)) {
				for(Client s : subscriptions.get(topic)) {
					if(s.getInBoundPortURI().equals(inboundPortURI))
						return true;
				}
			}
			return false;
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * @param inboundPortURI : the name of the  inboundPortURI
	 * @return the subscriber if he exists else null
	 * @throws Exception 
	 */
	private Client getSubscriber(String inboundPortURI) throws Exception { 
		try {
			readLock.lock();
			for(Client sub: subscribers) {
				if(sub.getInBoundPortURI().equals(inboundPortURI))
					return sub;
			}	
			return null;
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * Get a clone of list of subscibers for a topic 
	 * @param topic in question
	 * @return a clone of list of subscibers
	 */
	private ArrayList<Client> getSubscriptions(String topic) {
		readLock.lock();
		try {
			ArrayList<Client> sbs=new ArrayList<>();
			if(subscriptions.containsKey(topic)) {
				for(Client client : subscriptions.get(topic)) {
					sbs.add(client.copy()); 
				}
			}
			return sbs;
		}finally {
			readLock.unlock();
		}
		
	}

	/**=====================================================================================
	 * =================================== RECEPTIONCI =====================================
	 ======================================================================================*/
	
	/**
	 * Notify Subscribers and send them the message
	 * @param m the message to send
	 * @param topic where the message is publish
	 * @throws Exception
	 */
	public void sendMessage(MessageI m, String topic) throws Exception {
		
		//Connaitre le nombre de thread actif à un moment donnée
		//this.logMessage("Nb thread actif: "+Thread.activeCount());
		
		for(Client sub : getSubscriptions(topic)) {
			if(sub.hasFilter(topic)) {
				MessageFilterI f = sub.getFilter(topic);
				if (f.filter(m))
					sub.getPort().acceptMessage(m);
			}else {
				try {
					sub.getPort().acceptMessage(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * Notify Subscribers and send them messages
	 * @param ms list of messages to send
	 * @param topic where the message is publish
	 * @throws Exception
	 */
	public void sendMessages(MessageI[] ms, String topic) throws Exception {
		for(Client sub : getSubscriptions(topic)) {
			if(sub.hasFilter(topic)) {
				MessageFilterI f = sub.getFilter(topic);
				for(MessageI m : ms) {
					if (f.filter(m))
						sub.getPort().acceptMessage(m);
				}
				
			}else {
				try {
					for(MessageI m : ms) {
						sub.getPort().acceptMessage(m);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
