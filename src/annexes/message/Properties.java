package annexes.message;

import java.io.Serializable;
import java.util.HashMap;

public class Properties implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String,Object> map=new HashMap<>();
	
	public void putProp(String name, boolean v) {
		map.put(name, v);
	}

	public void putProp(String name, byte v) {
		map.put(name, v);
	}
	public void putProp(String name, char v) {
		map.put(name, v);
	}
	public void putProp(String name, double v) {
		map.put(name, v);
	}
	public void putProp(String name, float v) {
		map.put(name, v);
	}
	public void putProp(String name, int v) {
		map.put(name, v);
	}
	public void putProp(String name, long v) {
		map.put(name, v);
	}
	public void putProp(String name, short v) {
		map.put(name, v);
	}
	public void putProp(String name, String v) {
		map.put(name, v);
	}
	
	public boolean getBooleanProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Boolean)
			return (boolean)res;
		throw new Exception("Properties: not a boolean");
	}
	
	public byte getByteProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Byte)
			return (byte)res;
		throw new Exception("Properties: not a byte");
	}
	
	public char getCharProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Character)
			return (char)res;
		throw new Exception("Properties: not a char");
	}
	
	public double getDoubleProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Double)
			return (double)res;
		throw new Exception("Properties: not a double");
	}
	
	public float getFloatProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Float)
			return (float)res;
		throw new Exception("Properties: not a float");
	
	}
	
	public int getIntProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Integer)
			return (int)res;
		throw new Exception("Properties: not a int");
	}
	
	public long getLongProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Long)
			return (long)res;
		throw new Exception("Properties: not a long");
	}
	
	public short getShortProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof Short)
			return (short)res;
		throw new Exception("Properties: not a short");
	}
	
	public String  getStringProp(String name) throws Exception {
		Object res=map.get(name);
		if(res instanceof String)
			return (String)res;
		throw new Exception("Properties: not a String");
	}
}
