package annexes;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import annexes.message.interfaces.MessageI;

/**
 * This class <code>TopicKeeper </code> represent 
 * the storage of all the messages of a Broker
 * (a concurrent hashmap with topic as key and 
 * vector of message as value) 
 * 
 * @author Group LAMA
 *
 */
public class TopicKeeper {
	
	private volatile ConcurrentHashMap <String, Vector<String>> topics; //key: topic   value: list of id message
	
	
	/**
	 * Constructor of the concurrent hasmap that represent the storage of message for each topic
	 */
	public TopicKeeper() {
		topics = new ConcurrentHashMap <String, Vector<String>>();
	}
	
	/**
	 * Create a new topic 
	 * @param sujet to create in the hasmap
	 */
	public synchronized void createTopic(String sujet) {
		if(!topics.containsKey(sujet))
			topics.put(sujet, new Vector<String>());
	}
	
	/**
	 * Check if it's a topic that already exist
	 * @param sujet in question 
	 * @return if the topic exist in TopicKeeper
	 */
	public synchronized boolean isTopic(String sujet) {
		return topics.containsKey(sujet);
	}

	/**
	 * Create multiple new topics
	 * @param sujets the list of topic to create
	 */
	public synchronized void createTopics(String[] sujets) {
		for(String sujet: sujets) {
			if(!topics.containsKey(sujet))
				topics.put(sujet, new Vector<String>());
		}	
	}
	
	/**
	 * Remove a topic and all the messages of this topic
	 * @param sujet in question
	 */
	public synchronized void removeTopic(String sujet) {
		if(topics.containsKey(sujet)) {
			topics.remove(sujet);
			System.out.println("-- delete topic : "+sujet);
		}
	}
	
	/**
	 * Add a message in a specific topic
	 * @param sujet where to add the message
	 * @param m : a message
	 */
	public void addMessage(String sujet, MessageI m) {
		if(!topics.containsKey(sujet)) {
			topics.put(sujet, new Vector<String>());
			System.out.println("-- create topic : "+sujet);
		}
		topics.get(sujet).add(m.getURI());
		System.out.println("-- add message : "+m.getPayload()+" to topic : "+sujet);
	}
	
	/**
	 * Add a list of message in a specific topic
	 * @param sujet where to add the messages
	 * @param ms : the list of messages
	 */
	public void addMessages(String sujet, MessageI[] ms) {
		if(!topics.containsKey(sujet)) {
			topics.put(sujet, new Vector<String>());
			System.out.println("-- create topic : "+sujet);
		}
		
		for(MessageI m : ms) {
			topics.get(sujet).add(m.getURI());
			System.out.println("-- add message : "+m.getPayload()+" to topic : "+sujet);
		}
	}
	
	
	/**
	 * Getter of the list of topics that exist
	 * @return the list of all the topics
	 */
	public String[] getTopics() {
		Set<String> keys = topics.keySet();
		return keys.toArray(new String[0]);
	}
	
	/**
	 * Getter of the list of messages (d'un topic)
	 * @return the list of all the message of a topic
	 */
	public Vector<String> getMessages(String topic) {
		return topics.get(topic);
	}
	
	/**
	 * Check if the message already exist in the broker
	 * @param topic in question
	 * @param m : message to check the existence
	 * @return if the Broker has the message
	 */
	public boolean hasMessage(String topic, MessageI m) {
		if(topics.containsKey(topic)) {
			String id = m.getURI();
			return topics.get(topic).contains(id);

		}
		return false;
	}

}
