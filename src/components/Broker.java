package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import annexes.Subscription;
import ports.ManagementCInBoundPort;
import ports.PublicationCInBoundPort;
import ports.ReceptionCOutBoundPort;

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
	protected final String                managInboundPortPublisher;
	protected final String                managInboundPortSubscriber;
	protected Map <String, ArrayList<MessageI> >                  topics;          //<Topics, List of messages>
	protected Map <String, ReceptionCOutBoundPort>                subscribers;     // List of Subscriber
	protected Map <String, ArrayList<Subscription> >              subscription;    //<Topics, List of Subscriber>
	private int cpt;
	
	
	
	protected Broker(int nbThreads, int nbSchedulableThreads, 
							String uri, 
							String managInboundPortPublisher,
							String managInboundPortSubscriber, 
							String publicationInboundPortURI) throws Exception {
		super(uri, nbThreads, nbSchedulableThreads);
		
		assert managInboundPortPublisher != null;
		assert managInboundPortSubscriber != null;
		assert publicationInboundPortURI != null;
		
		this.managInboundPortPublisher = managInboundPortPublisher;
		this.managInboundPortSubscriber = managInboundPortSubscriber;
		cpt=0;
		
		topics = new HashMap <String, ArrayList<MessageI>>();  
		subscription = new HashMap <String, ArrayList<Subscription>>();  
		subscribers = new HashMap <String, ReceptionCOutBoundPort>();
		
		
		/**----------------- ADD COMPONENTS -------------------*/
		this.addOfferedInterface(ManagementCI.class);
		this.addOfferedInterface(PublicationCI.class);
		this.addRequiredInterface(ReceptionCI.class);
		
		/**---------------- PORTS CREATION --------------------*/
		this.mipPublisher = new ManagementCInBoundPort(managInboundPortPublisher,this);
		this.mipSubscriber = new ManagementCInBoundPort(managInboundPortSubscriber,this);
		this.publicationInboundPort = new PublicationCInBoundPort(publicationInboundPortURI, this);
		
		
		/**-------------- PUBLISH PORT IN REGISTER ------------*/
		this.mipPublisher.publishPort(); 
		this.mipSubscriber.publishPort(); 
		this.publicationInboundPort.publishPort();
	}
	
	
	/**-----------------------------------------------------
	 * -------------------- LIFE CYCLE ---------------------
	 ------------------------------------------------------*/
	/**
	 * 
	 */
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
		
		super.finalise();
	}
	
	
	
	/**======================================================================================
	 * ================================== PUBLICATIONCI =====================================
	 ======================================================================================*/

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		ArrayList<MessageI> n;
		if(isTopic(topic)) 
			n = topics.get(topic);
		else 
			n = new ArrayList<>();
		
		n.add(m);
		topics.put(topic, n);
		this.logMessage("Broker: Message publié dans "+topic);
		
		//Notify Subscribers
		if(subscription.containsKey(topic)) { 
			for(Subscription sub : subscription.get(topic)) {
				
				sub.getPort().acceptMessage(m);
				this.logMessage("broker: je passe par la");
			}
		}
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		for(String t: topics) {
			publish(m,t);
		}
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		ArrayList<MessageI> n;
		if(isTopic(topic)) 
			n = topics.get(topic);
		else 
			n = new ArrayList<>();
		
		for(MessageI m: ms) {
			n.add(m);
		}
		topics.put(topic, n);
		this.logMessage("Broker: Messages publiés dans "+topic);
		
		//Notify Subscribers
		if(subscription.containsKey(topic)) {
			for(Subscription sub : subscription.get(topic)) {
				for(MessageI m: ms) {
					sub.getPort().acceptMessage(m);
				}
			}
		}
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		for(String t: topics) {
			publish(ms,t);
		}
	}

	
	/**======================================================================================
	 * =================================== MANAGEMENTCI =====================================
	 ======================================================================================*/
	@Override
	public void createTopic(String topic) throws Exception {
		if(!isTopic(topic))
			topics.put(topic, new ArrayList<MessageI>());
	}

	@Override
	public void createTopics(String[] topics) throws Exception {
		for(String t : topics)
			createTopic(t);
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		if(isTopic(topic)) {
			topics.remove(topic); // /!\ être sur d'avoir délivrer les messages avant la destruction du topic. 
			subscription.remove(topic);
		}
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return topics.containsKey(topic);
	}

	@Override
	public String[] getTopics() throws Exception {
		Set<String> keys = topics.keySet();
		String[] tops = keys.toArray(new String[0]);
		return tops;
	}


	/**------------------------------------------------------
	 * ----------------- (UN)SUBSCRIBE ----------------------
	 -------------------------------------------------------*/
	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.subscribe(topic, null, inboundPortURI);
	}

	@Override
	public void subscribe(String[] topics, String inboundPortURI) throws Exception {
		for(String t: topics)
			this.subscribe(t,null, inboundPortURI);
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		if(!isTopic(topic))
			this.createTopic(topic);
		
		if(!isSubscribe(topic, inboundPortURI)) {
			ArrayList<Subscription> subs;
			if (subscription.containsKey(topic)) //Si le topic contient déjà des subscibers
				subs = subscription.get(topic);  //On recupere les subscriptions
			else
				subs = new ArrayList<>();	     //Sinon on crée une nouvelle liste associé au topic
			
			ReceptionCOutBoundPort port = getSubscriber(inboundPortURI);

			if(filter!=null)
				subs.add(new Subscription(port, filter));
			else
				subs.add(new Subscription(port));
			
			subscription.put(topic, subs);
			this.logMessage("Le subscriber "+inboundPortURI+" subscrit au topic "+topic);
		}
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		if(!subscription.containsKey(topic)) 
			return;
		if(!subscribers.containsKey(inboundPortURI)) 
			return;

		for(Subscription s : subscription.get(topic)) {
			if(s.getUri().equals(inboundPortURI)) {
				s.setFilter(newFilter);
				break;
			}
		}	
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		if(subscription.containsKey(topic)) {
			Subscription trouve = null;
			for(Subscription s : subscription.get(topic)) {
				if(s.getUri().equals(inboundPortURI)) {
					trouve = s;
					break;
				}
			}
			if(trouve != null) {
				this.logMessage("Unsubscribe of "+trouve.getUri()+" for the topic "+topic);
				subscription.get(topic).remove(trouve);
			}else {
				this.logMessage(inboundPortURI+" already unsubscribe");
			}
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
		if(subscription.containsKey(topic)) {
			ArrayList<Subscription> subs = subscription.get(topic);
			for(Subscription s : subs) {
				if(s.getUri().equals(inboundPortURI))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * @param inboundPortURI : the name of the  inboundPortURI
	 * @return the subscriber if he exists else null
	 * @throws Exception 
	 */
	private ReceptionCOutBoundPort getSubscriber(String inboundPortURI) throws Exception { 
		if(subscribers.containsKey(inboundPortURI))
			return subscribers.get(inboundPortURI);
		
		//Creation of a new port for the subscriber
		String portURI = "br"+cpt+"oport";
		cpt++;
		ReceptionCOutBoundPort port = new ReceptionCOutBoundPort(portURI, this);
		port.publishPort();
		this.doPortConnection(
				portURI,
				inboundPortURI, 
				ReceptionConnector.class.getCanonicalName()) ;
		//Update list of subscribers
		subscribers.put(inboundPortURI, port);
		return port;
	}
	
}
