package annexes.message;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class <code>Properties</code> represent
 * the properties of a message (in a hashmap)
 * 
 * @author Group LAMA
 *
 */
public class Properties implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private HashMap<String,Object> map = new HashMap<>();
	
	/**
	 * Check if there is a property of this name
	 * @param name of the property
	 * @return if the property exist 
	 */
	public boolean contains(String name) {
		if (map.containsKey(name))
			return true;
		return false;
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> boolean
	 */
	public void putProp(String name, boolean v) {
		map.put(name, v);
	}

	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> byte
	 */
	public void putProp(String name, byte v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> char
	 */
	public void putProp(String name, char v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> double
	 */
	public void putProp(String name, double v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> float
	 */
	public void putProp(String name, float v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> int
	 */
	public void putProp(String name, int v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> long
	 */
	public void putProp(String name, long v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> short
	 */
	public void putProp(String name, short v) {
		map.put(name, v);
	}
	
	/**
	 * Insert a property 
	 * @param name of the property -> String
	 * @param v the value -> String
	 */
	public void putProp(String name, String v) {
		map.put(name, v);
	}
	
	
	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> boolean
	 * @throws Exception
	 */
	public boolean getBooleanProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Boolean)
				return (boolean)res;
			throw new Exception("Properties: not a boolean");
		}
		return false;
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> byte
	 * @throws Exception
	 */
	public byte getByteProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Byte)
				return (byte)res;
			throw new Exception("Properties: not a byte");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> char
	 * @throws Exception
	 */
	public char getCharProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Character)
				return (char)res;
			throw new Exception("Properties: not a char");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> double
	 * @throws Exception
	 */
	public double getDoubleProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Double)
				return (double)res;
			throw new Exception("Properties: not a double");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> float
	 * @throws Exception
	 */
	public float getFloatProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Float)
				return (float)res;
			throw new Exception("Properties: not a float");
		}
		throw new Exception("Properties: do not exist");

	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> int
	 * @throws Exception
	 */
	public int getIntProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Integer)
				return (int)res;
			throw new Exception("Properties: not a int");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> long
	 * @throws Exception
	 */
	public long getLongProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Long)
				return (long)res;
			throw new Exception("Properties: not a long");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param  name of the property -> String
	 * @return the value of the property -> short
	 * @throws Exception
	 */
	public short getShortProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof Short)
				return (short)res;
			throw new Exception("Properties: not a short");
		}
		throw new Exception("Properties: do not exist");
	}

	/**
	 * Get the property describe by the name
	 * @param name of the property -> String
	 * @return the value of the property -> String
	 * @throws Exception
	 */
	public String  getStringProp(String name) throws Exception {
		if(map.containsKey(name)) {
			Object res=map.get(name);
			if(res instanceof String)
				return (String)res;
			throw new Exception("Properties: not a String");
		}
		throw new Exception("Properties: do not exist");
	}
}
