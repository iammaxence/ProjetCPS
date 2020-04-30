package interfaces;

import annexes.message.interfaces.MessageI;

/**
 * Interface <code>PublicationsImplementationI</code>
 * all methods for management of publication
 * 
 * @author GROUP LAMA
 *
 */
public interface PublicationsImplementationI {
	public void publish(MessageI m,String topic)throws Exception;
	public void publish(MessageI m,String[] topics)throws Exception;
	public void publish(MessageI[] ms,String topic)throws Exception;
	public void publish(MessageI[] ms,String[] topics)throws Exception;
}
