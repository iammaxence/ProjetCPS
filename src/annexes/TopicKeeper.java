package annexes;

import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import annexes.message.interfaces.MessageI;

/**
 * 
 * @author Group LAMA
 * This class represent the storage of all the messages of a Broker
 * (a concurrent hashmap with topic as key and vector of message as value) 
 *
 */
public class TopicKeeper {
	
	private volatile ConcurrentHashMap <String, Vector<MessageI>> topics; //key: topic   value: list of message
	
	
	/**
	 * Constructor of the concurrent hasmap that represent the storage of message for each topic
	 */
	public TopicKeeper() {
		topics = new ConcurrentHashMap <String, Vector<MessageI>>();
	}
	
	/**
	 * Create a new topic 
	 * @param sujet to create in the hasmap
	 */
	public synchronized void createTopic(String sujet) {
		if(!topics.containsKey(sujet))
			topics.put(sujet, new Vector<MessageI>());
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
				topics.put(sujet, new Vector<MessageI>());
		}	
	}
	
	/**
	 * Remove a topic and all the messages of this topic
	 * @param sujet in question
	 */
	public synchronized void removeTopic(String sujet) {
		if(topics.containsKey(sujet))
			topics.remove(sujet);
	}
	
	/**
	 * Add a message in a specific topic
	 * @param sujet where to add the message
	 * @param m : a message
	 */
	public void addMessage(String sujet, MessageI m) {
		if(!topics.containsKey(sujet)) 
			topics.put(sujet, new Vector<MessageI>());
		topics.get(sujet).add(m);
	}
	
	/**
	 * Add a list of message in a specific topic
	 * @param sujet where to add the messages
	 * @param ms : the list of messages
	 */
	public void addMessages(String sujet, MessageI[] ms) {
		if(!topics.containsKey(sujet)) 
			topics.put(sujet, new Vector<MessageI>());
		
		for(MessageI m : ms)
			topics.get(sujet).add(m);
	}
	
	/**
	 * Getter of the list of topics that exist
	 * @return the list of all the topics
	 */
	public String[] getTopics() {
		Set<String> keys = topics.keySet();
		return keys.toArray(new String[0]);
	}

}
