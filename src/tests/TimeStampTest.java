package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import annexes.message.TimeStamp;


/**
 * This JUnit class test all the 
 * method of the class TimeStamp
 * (in annexe)
 * 
 * @author Group LAMA
 *
 */
public class TimeStampTest {

	/**
	 * Test all methods of TimeSTamp
	 */
	@Test
	public void test() {
 		TimeStamp ts = new TimeStamp( 1, "test");
//		assertFalse(ts.isInitialised());
//		assertEquals(-1, ts.getTime());
		
		ts.setTime(10);
		assertEquals(10, ts.getTime());
		
		ts.setTimeStamper("Fevrier");
		assertEquals("Fevrier", ts.getTimeStamper());
		
		ts.setTimeStamper("jeudi");
		assertNotEquals("Fevrier", ts.getTimeStamper());
		
	}

}
