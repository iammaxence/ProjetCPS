package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import annexes.message.Properties;

public class PropertiesTest {
	protected Properties props = new Properties();
	protected String[] names = {"boolean", "byte", "char", "double","float", "int", "long", "short","string"};
	
	/**
	 * Check all methods of properties 
	 */
	@Test
	public void test() {
		boolean bool = true;
		props.putProp(names[0], bool);
			
		byte b = 1;
		props.putProp(names[1], b);
		
		char c = 'c';
		props.putProp(names[2], c);
		
		double d = 2.0;
		props.putProp(names[3], d);
		
		float f = 1/2;
		props.putProp(names[4], f);
		
		int i = 1;
		props.putProp(names[5], i);
		
		long l = 64;
		props.putProp(names[6], l);
		
		short sh =16;
		props.putProp(names[7], sh);
		
		String str = "myproperty";
		props.putProp(names[8], str);
		
		try {
			assertTrue(props.contains(names[0]));
			assertTrue(props.getBooleanProp(names[0]));
			assertEquals(b, props.getByteProp(names[1]));
			assertEquals(c, props.getCharProp(names[2]));
			assertTrue(d == props.getDoubleProp(names[3]));
			assertTrue(f == props.getFloatProp(names[4]));
			assertTrue(i == props.getIntProp(names[5]));
			assertTrue(l == props.getLongProp(names[6]));
			assertTrue(sh == props.getShortProp(names[7]));
			assertTrue(str.equals(props.getStringProp(names[8])));
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}

}
