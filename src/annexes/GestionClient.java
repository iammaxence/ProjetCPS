package annexes;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import annexes.message.interfaces.MessageFilterI;

/**
 * 
 * @author Group LAMA
 * This class manage all the subscription of a Broker, it's confined in this class
 * and it use concurrent hashmap
 *
 */
public class GestionClient {
	private volatile ConcurrentHashMap <String, Vector<String>>   souscriptions;  // key: topic value: list of subscriber
	private ConcurrentHashMap <String, Client>                    subscribers;    // list of all the subscriber link to the broker
	
	/**
	 * Constructor of GestionClient
	 */
	public GestionClient() {
		souscriptions = new ConcurrentHashMap<String, Vector<String>>();
		subscribers = new ConcurrentHashMap <String, Client>();		
	}
	
	/**
	 * 
	 * @param topic
	 * @param inboundPortURI
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
	 * 
	 * @param inboundPortURI
	 * @return
	 */
	public synchronized boolean isClient(String inboundPortURI) {
		return subscribers.containsKey(inboundPortURI);
	}
	
	/***
	 * 
	 * @param inboundPortURI
	 * @param client
	 */
	public synchronized void addClient(String inboundPortURI, Client client) {
		if(!subscribers.containsKey(inboundPortURI)) {
			subscribers.put(inboundPortURI, client);
			System.out.println("-- create client : "+client.getOutBoundPortURI());
		}
	}
	
	/**
	 * 
	 * @param inboundPortURI
	 * @param topic
	 * @param filter
	 */
	public void modifyFilterOf(String inboundPortURI, String topic, MessageFilterI filter) {
		if(subscribers.containsKey(inboundPortURI)) 
			subscribers.get(inboundPortURI).setFilter(filter, topic);
	}

	/**
	 * 
	 * @param inboundPortURI
	 * @param topic
	 */
	public void unsubscribe(String inboundPortURI, String topic) {
		if(souscriptions.get(topic).contains(inboundPortURI)) 
			souscriptions.get(topic).remove(inboundPortURI);
	}
	
	/**
	 * 
	 * @param topic
	 * @return
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
	 * 
	 * @param topic
	 * @param inboundPortURI
	 * @return
	 */
	public boolean isSubscribe(String topic, String inboundPortURI) {
		if (souscriptions.containsKey(topic))
			return souscriptions.get(topic).contains(inboundPortURI);
		return false;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Collection<Client> getAllSubscribers(){
		return subscribers.values();
	}
	
	
}
