package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import annexes.TopicKeeper;
import annexes.message.Message;
import annexes.message.interfaces.MessageI;


/**
 * This JUnit class test all the 
 * method of the class TopicKeeper
 * (in annexe)
 * 
 * @author Group LAMA
 *
 */
public class TopicKeeperTest {
	
	protected static TopicKeeper tk;
	protected final static String topic = "test";
	protected final static String[] topics = {"topic1", "topic2", "topic3"};

	@BeforeClass
	public static void initialise() throws Exception {
		tk = new TopicKeeper();
	}
	
	/**
	 * Pour etre sur de l'ordre des instructions celles-ci 
	 * sont donc reunis dans un seul test testant les topics
	 * et les messages
	 */
	@Test
	public void tests() {	
		//Test add and delete one topic
		assertFalse(tk.isTopic(topic));
		tk.createTopic(topic);
		assertTrue(tk.isTopic(topic));
		
		tk.removeTopic(topic);
		assertFalse(tk.isTopic(topic));
		
		assertEquals(tk.getTopics().length, 0);
		
		
		//Test add multiple topics and delete one
		tk.createTopics(topics);
		assertTrue(tk.isTopic(topics[0]));
		assertTrue(tk.isTopic(topics[1]));
		assertTrue(tk.isTopic(topics[2]));
		
		assertEquals(tk.getTopics().length, 3);
		
		tk.removeTopic(topics[1]);
		assertFalse(tk.isTopic(topics[1]));
		
		assertEquals(tk.getTopics().length, 2);
		
		//------------- Test Messages -----------------
		Message msg = new Message("Hello World!");
		MessageI[] messages = {new Message("Message 1"), new Message("Message 2"), new Message("Message 3")};
		
		tk.addMessage(topic, msg);
		assertTrue(tk.getMessages(topic).size() == 1);
		
		tk.addMessages(topics[0], messages);
		assertTrue(tk.hasMessage(topics[0], messages[0]));
		assertTrue(tk.getMessages(topics[0]).size() == 3);
		
	}

}
