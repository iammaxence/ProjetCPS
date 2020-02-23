package annexes.message;

import java.io.Serializable;

import annexes.message.interfaces.MessageI;

public class Message 
implements MessageI{
	
	private static final long serialVersionUID = 1L;
	private static int cpt=0;
	
	private final String uri;
	private String message;
	private TimeStamp ts;
	private Properties properties;
	
	public Message(String msg) {
		uri = "Msg"+cpt++; //ex: Msg12 avec id unique
		ts = new TimeStamp(); //Non initialisé
		this.message = msg;
		properties = new Properties();
	}
	
	public Message(String msg, TimeStamp ts, Properties props) {
		uri="msg"+cpt++; 
		this.message=msg;
		this.ts=ts;
		properties=props;
	}
	
	@Override
	public String getURI() {	
		return uri;
	}

	@Override
	public TimeStamp getTimeStamp() {
		return ts;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public Serializable getPayload() {
		return message;
	}

}
