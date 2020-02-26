package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import annexes.message.Message;
import annexes.message.Properties;
import annexes.message.TimeStamp;
import annexes.message.interfaces.MessageI;

public class MessageTest {
	protected static MessageI message;
	
	@BeforeClass
	public static void initialise() throws Exception {
		message = new Message("Hello Word");
	}

	@Test
	public final void testUri() {
		String uri = "Msg1";
		assertEquals(uri, message.getURI());  /* C'est le premier message */
	}
	
	@Test
	 public final void testPayload() {
		String msg = "Hello Word";
		assertEquals(msg, message.getPayload());
	}
	
	@Test
	 public final void testTimeStamp() {
		TimeStamp ts = new TimeStamp();
		Message m = new Message("Hello Word", ts, null);
		assertNotNull(m.getTimeStamp());
		assertEquals(ts, m.getTimeStamp());
	}
	
	@Test
	 public final void testProperties() {
		String msg = "Hello Word";
		Properties props = new Properties();
		Message m = new Message(msg,null,props);
		assertNotNull(m.getProperties());
		assertEquals(props, m.getProperties());
	}
	
	
	
	
	
	
	

}
