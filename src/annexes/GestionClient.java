package annexes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import annexes.message.interfaces.MessageFilterI;

/**
 * This class <code> </code> manage all subscriptions
 * of the Broker, it's confine in this class and it 
 * use a concurrent hashmap.
 * 
 * @author Group LAMA
 *
 */
public class GestionClient {
	private volatile ConcurrentHashMap <String, Vector<String>>   souscriptions;  // key: topic value: list of subscriber
	private ConcurrentHashMap <String, Client>                    subscribers;    // list of all the subscriber link to the broker
	
	/**
	 * Constructor of GestionClient
	 * Creation of the subscriptions and all the subscribers of the Broker
	 */
	public GestionClient() {
		souscriptions = new ConcurrentHashMap<String, Vector<String>>();
		subscribers = new ConcurrentHashMap <String, Client>();		
	}
	
	
	/**
	 * Add a subscription in the concurrent hasmap and if it's the first time
	 * in the list of all subscribers too.
	 * @param topic in question
	 * @param inboundPortURI of the subscriber
	 * @param filter of the topic if there is one
	 * @return if the subscriber has been added
	 */
	public boolean addSouscription(String topic, String inboundPortURI, MessageFilterI filter) {
		//Check the existence of subscriber
		if(subscribers.containsKey(inboundPortURI)) {
			//Add the filter if it's exist to the topic that the subscriber choose
			if(filter != null)
				subscribers.get(inboundPortURI).setFilter(filter, topic);
			
			//Create a topic if it's not exist
			if(!souscriptions.containsKey(topic))
				souscriptions.put(topic, new Vector<String>());
			
			// if the subscriber already subscribe, it'll not be added 
			if(!souscriptions.get(topic).contains(inboundPortURI)) {
				souscriptions.get(topic).add(inboundPortURI);	
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param inboundPortURI of the Subscriber
	 * @return if it's the first time that the client subscribe or not
	 */
	public boolean isClient(String inboundPortURI) {
		return subscribers.containsKey(inboundPortURI);
	}
	
	/***
	 * Add a client in the list of all subscribers
	 * @param inboundPortURI of the client
	 * @param client to add if it's the first time
	 */
	public void addClient(String inboundPortURI, Client client) {
		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, client);
			System.out.println("-- create client : "+client.getOutBoundPortURI());
		}
	}
	
	/**
	 * Modify the filter message of a subscriber for a specific topic
	 * @param inboundPortURI of the Subscriber
	 * @param topic in question
	 * @param filter for messages 
	 */
	public void modifyFilterOf(String inboundPortURI, String topic, MessageFilterI filter) {
		if(subscribers.containsKey(inboundPortURI)) 
			subscribers.get(inboundPortURI).setFilter(filter, topic);
	}

	/**
	 * Unsubscribe the subscriber of a topic
	 * @param inboundPortURI of the Subscriber
	 * @param topic to unsubscribre
	 */
	public void unsubscribe(String inboundPortURI, String topic) {
		if(souscriptions.get(topic).contains(inboundPortURI)) 
			souscriptions.get(topic).remove(inboundPortURI);
	}
	
	/**
	 * Get list of subscribers for a specific topic
	 * @param topic of the request
	 * @return the list of subscribers for this topic
	 */
	public ArrayList<Client> getSubscribersOfTopic(String topic){
		ArrayList<Client> res = new ArrayList<Client>();
		if(souscriptions.containsKey(topic)) {
			for(String uri: souscriptions.get(topic)) {
				res.add(subscribers.get(uri).copy());
			}
		}
		System.out.println("-- getSubscribers of "+topic+" : "+res.size());
		return res;
	}

	/**
	 * Check if the subscriber it's subscribe to a specific topic
	 * @param topic of the request
	 * @param inboundPortURI of the Subscriber
	 * @return true if he follow this topic
	 */
	public boolean isSubscribe(String topic, String inboundPortURI) {
		if (souscriptions.containsKey(topic))
			return souscriptions.get(topic).contains(inboundPortURI);
		return false;
	}
	
	
	/**
	 * Get all the subscriber of the Broker
	 * @return a collection of all Client link to the Broker
	 */
	public Collection<Client> getAllSubscribers(){
		return subscribers.values();
	}
	
	
}
