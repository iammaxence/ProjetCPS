package components;

import java.util.ArrayList;
import java.util.Collection;

import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
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
import annexes.GestionClient;
import annexes.TopicKeeper;
import ports.ManagementCInBoundPort;
import ports.PublicationCInBoundPort;
import ports.ReceptionCOutBoundPort;

/**
 * The class Broker 
 * 
 * @author GROUP LAMA
 *
 */
@OfferedInterfaces(offered= {ManagementCI.class,PublicationCI.class})
@RequiredInterfaces(required = {ReceptionCI.class} )
public class Broker 
extends AbstractComponent 
implements ManagementImplementationI, SubscriptionImplementationI, PublicationsImplementationI{


	/**------------------- PORTS -------------------------*/
	protected ManagementCInBoundPort      mipPublisher;   // Connected to URIPublisher 
	protected ManagementCInBoundPort      mipSubscriber;  // Connected to URISubscriber 
	protected PublicationCInBoundPort     publicationInboundPort;
	
	
	/**------------------ VARIABLES ----------------------*/
	protected TopicKeeper                                     topics;        
	protected GestionClient                                   subscriptions;   
	private int threadPublication, threadSubscription, threadEnvoi;
	
	/**----------------------- MUTEX ----------------------*/
//	protected ReadWriteLock lock = new ReentrantReadWriteLock();
//	protected Lock readLock = lock.readLock();
//	protected Lock writeLock = lock.writeLock();
	
	
	
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
		subscriptions = new GestionClient();
		
		/**---------------------- THREADS ---------------------*/
		threadPublication = createNewExecutorService("threadPublication", 10, false);
		threadSubscription = createNewExecutorService("threadSubscription", 10, false);
		threadEnvoi = createNewExecutorService("threadEnvoi", 10, true);
		
		
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
		Collection<Client> clients = subscriptions.getAllSubscribers();
		for(Client c : clients) {
			c.unpublish();
			this.doPortDisconnection(c.getOutBoundPortURI());
		}
		
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
		//Creation of the Client if it's the first time 
		if(!subscriptions.isClient(inboundPortURI)) {                  // /!\ NE PAS OUBLIER DE LOCK
			Client c = new  Client(inboundPortURI, new ReceptionCOutBoundPort(this));
			this.doPortConnection(c.getOutBoundPortURI(), 
								  inboundPortURI, 
								  ReceptionConnector.class.getCanonicalName());
			
			subscriptions.addClient(inboundPortURI, c);
		}
		
		if(subscriptions.addSouscription(topic, inboundPortURI, filter)) {
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
		subscriptions.modifyFilterOf(inboundPortURI, topic, newFilter);
	}

	/**
	 * Method of SubsciptionImplementationI
	 * @param topic where the subscriber want to unsubscribe
	 * @param inboundPortURI of the Subscriber
	 * @throws Exception 
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		subscriptions.unsubscribe(inboundPortURI, topic);
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
		
		ArrayList<Client> clients = subscriptions.getSubscribersOfTopic(topic);
		
		
		//Creation of a task: filter and send message
		AbstractComponent.AbstractService<Void> task = new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				boolean envoyer;
				for(Client sub : clients) {
					envoyer = true;
					if(sub.hasFilter(topic)) {
						MessageFilterI f = sub.getFilter(topic);
						envoyer = f.filter(m);
					}
					try {
						if(envoyer)
							sub.getPort().acceptMessage(m);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				return null;
			}
		};
		this.handleRequest(threadEnvoi, task);		
	}
	
	/**
	 * Notify Subscribers and send them messages
	 * @param ms list of messages to send
	 * @param topic where the message is publish
	 * @throws Exception
	 */
	public void sendMessages(MessageI[] ms, String topic) throws Exception {
		//Get the list of subscriber for the topic;
		ArrayList<Client> clients = subscriptions.getSubscribersOfTopic(topic);

		//Creation of a task: filter and send message
		AbstractComponent.AbstractService<Void> task = new AbstractComponent.AbstractService<Void>() {

			@Override
			public Void call() throws Exception {
				for(Client sub : clients) {
					try {
						//Get the filter if there is one
						if(sub.hasFilter(topic)) {
							MessageFilterI f = sub.getFilter(topic);
							for(MessageI m : ms) {
								if (f.filter(m))
									sub.getPort().acceptMessage(m);
							}
						}else {
							for(MessageI m : ms) {
									sub.getPort().acceptMessage(m);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}

				}
				return null;
			}
		};
		this.handleRequest(threadEnvoi, task);	
		
	}

}
