package interfaces;

import annexes.message.interfaces.MessageI;

public interface PublicationsImplementationI {
	
	public void publish(MessageI m,String topic)throws Exception;
	public void publish(MessageI m,String[] topics)throws Exception;
	public void publish(MessageI[] ms,String topic)throws Exception;
	public void publish(MessageI[] ms,String[] topics)throws Exception;
}
