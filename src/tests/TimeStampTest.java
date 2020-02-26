package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import annexes.message.TimeStamp;

public class TimeStampTest {

	@Test
	public void test() {
		TimeStamp ts = new TimeStamp();
		assertFalse(ts.isInitialised());
		assertEquals(-1, ts.getTime());
		
		ts.setTime(10);
		assertEquals(10, ts.getTime());
		
		ts.setTimeStamper("Fevrier");
		assertEquals("Fevrier", ts.getTimeStamper());
		
		ts.setTimeStamper("jeudi");
		assertNotEquals("Fevrier", ts.getTimeStamper());
		
	}

}