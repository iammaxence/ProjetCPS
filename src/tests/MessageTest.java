package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import annexes.message.Message;
import annexes.message.Properties;
import annexes.message.TimeStamp;
import annexes.message.interfaces.MessageI;

/**
 * This JUnit class test all the 
 * method of the class Message
 * (in annex)
 * 
 * @author Group LAMA
 *
 */
public class MessageTest {
	protected static MessageI message;
	
	@BeforeClass
	public static void initialise() throws Exception {
		message = new Message("Hello Word");
	}

	/**
	 * Check if getURI return the URI of the message
	 */
	@Test
	public final void testUri() {
		String uri = "MESSAGE_1";
		assertEquals(uri, message.getURI());  /* C'est le premier message */
	}
	
	/**
	 * Check if getPayload return the message
	 */
	@Test
	 public final void testPayload() {
		String msg = "Hello Word";
		assertEquals(msg, message.getPayload());
	}
	
	/**
	 * Check if getTimeStamp return the timeStamp
	 */
	@Test
	 public final void testTimeStamp() {
		TimeStamp ts = new TimeStamp();
		Message m = new Message("Hello Word", ts, null);
		assertNotNull(m.getTimeStamp());
		assertEquals(ts, m.getTimeStamp());
	}
	
	/**
	 * Chech if getProperties return the properties
	 * with the properties put in the constructor
	 */
	@Test
	 public final void testProperties1() {
		String msg = "Hello Word";
		Properties props = new Properties();
		props.putProp("Hello", true);
		Message m = new Message(msg,null,props);
		assertNotNull(m.getProperties());
		assertEquals(props, m.getProperties());
		try {
			assertTrue(m.getProperties().getBooleanProp("Hello"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Chech if getProperties return the properties
	 * without the properties put in the constructor
	 */
	@Test
	 public final void testProperties2() {
		String msg = "Hello Word";
		Message m = new Message(msg);
		assertNotNull(m.getProperties());
		try {
			assertTrue(m.getProperties().getBooleanProp("Hello"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
