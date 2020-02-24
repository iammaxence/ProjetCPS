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
import annexes.Client;
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
	protected ArrayList<Client>                             subscribers;     // List of Subscriber
	protected Map <String, ArrayList<Client> >              subscriptions;    //<Topics, List of Subscriber>
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
		subscriptions = new HashMap <String, ArrayList<Client>>();  
		subscribers = new ArrayList<Client>();
		
		
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

		//Notify Subscribers
		if(subscriptions.containsKey(topic)) { 
			for(Client sub : subscriptions.get(topic)) {
				sub.getPort().acceptMessage(m);
			}
		}
		this.logMessage("Broker: Message publié dans "+topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		for(String t: topics) {
			this.publish(m,t);
		}
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		for(MessageI m: ms)
			this.publish(m, topic);
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		for(String t: topics) {
			this.publish(ms,t);
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
			subscriptions.remove(topic);
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
			this.subscribe(t, null, inboundPortURI);
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
		if(!isTopic(topic))
			this.createTopic(topic);

		Client s;
		if(!isSubscribe(topic, inboundPortURI)) {
			ArrayList<Client> subs;
			if (subscriptions.containsKey(topic)) //Si le topic contiens déjà des subscibers
				subs = subscriptions.get(topic); //On les recupèrent (les abonnées)
			else
				subs = new ArrayList<>();	//Sinon on crée une nouvelle liste associé au topic
			
			
			Client monSub = getSubscriber(inboundPortURI);
			
			if(monSub==null) { // Si le sub n'est pas dans la liste officiel des subscibers existants
				String portURI = "uri-"+cpt++;
				monSub = new Client(inboundPortURI, new ReceptionCOutBoundPort(portURI, this));  // Je crée le sub avec un port unique
				monSub.getPort().publishPort();
				this.doPortConnection(
						monSub.getOutBoundPortURI(),
						inboundPortURI, 
						ReceptionConnector.class.getCanonicalName()) ;
				
				subscribers.add(monSub); // J'ajoute le sub nouvellement crée dans la liste officiel des subscibers
				
			}
			
			// Je l'ajoute dans la liste de ceux abonnée au topic
			subs.add(monSub);
			subscriptions.put(topic, subs);
			
			this.logMessage("Subscriber "+inboundPortURI+" subscribe to topic "+topic);
		}else {
			this.logMessage("Subscriber "+inboundPortURI+" already subscribe to topic "+topic);
		}


	}

	
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortURI) throws Exception {
		if(!subscriptions.containsKey(topic)) 
			return;

		for(Client s : subscriptions.get(topic)) {
			if(s.getInBoundPortURI().equals(inboundPortURI)) {
				s.setFilter(newFilter);
				break;
			}
		}	
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		if(subscriptions.containsKey(topic)) {
			Client trouve = null;
			for(Client s : subscriptions.get(topic)) {
				if(s.getInBoundPortURI().equals(inboundPortURI)) {
					trouve = s;
					break;
				}
			}
			if(trouve != null) {
				this.logMessage("Unsubscribe of "+trouve.getOutBoundPortURI()+" for the topic "+topic);
				subscriptions.get(topic).remove(trouve);
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
		if(subscriptions.containsKey(topic)) {
			ArrayList<Client> subs = subscriptions.get(topic);
			for(Client s : subs) {
				if(s.getInBoundPortURI().equals(inboundPortURI))
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
	private Client getSubscriber(String inboundPortURI) throws Exception { 
		for(int i=0; i< subscribers.size() ; i++){
			Client sub = subscribers.get(i);
			if(sub.getInBoundPortURI().equals(inboundPortURI))
				return sub;
		}	
		return null;
	}

	
}
