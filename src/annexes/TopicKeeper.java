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
	
	private volatile ConcurrentHashMap <String, Vector<MessageI>> topics;
	
	
	/**
	 * 
	 */
	public TopicKeeper() {
		topics = new ConcurrentHashMap <String, Vector<MessageI>>();
	}
	
	/**
	 * 
	 * @param sujet
	 */
	public void createTopic(String sujet) {
		if(!topics.contains(sujet))
			topics.put(sujet, new Vector<MessageI>());
	}
	
	/**
	 * 
	 * @param sujet
	 * @return
	 */
	public boolean isTopic(String sujet) {
		return topics.contains(sujet);
	}

	/**
	 * 
	 * @param sujets
	 */
	public void createTopics(String[] sujets) {
		for(String sujet: sujets) {
			if(!topics.contains(sujet))
				topics.put(sujet, new Vector<MessageI>());
		}	
	}
	
	/**
	 * 
	 * @param sujet
	 */
	public void removeTopic(String sujet) {
		if(topics.contains(sujet))
			topics.remove(sujet);
	}
	
	/**
	 * 
	 * @param sujet
	 * @param m
	 */
	public void addMessage(String sujet, MessageI m) {
		if(!topics.contains(sujet)) 
			topics.put(sujet, new Vector<MessageI>());
		topics.get(sujet).add(m);
	}
	
	/**
	 * 
	 * @param sujet
	 * @param ms
	 */
	public void addMessages(String sujet, MessageI[] ms) {
		if(!topics.contains(sujet)) 
			topics.put(sujet, new Vector<MessageI>());
		
		for(MessageI m : ms)
			topics.get(sujet).add(m);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getTopics() {
		Set<String> keys = topics.keySet();
		return keys.toArray(new String[0]);
	}

}
