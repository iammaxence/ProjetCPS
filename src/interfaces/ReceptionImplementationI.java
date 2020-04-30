package interfaces;

import annexes.message.interfaces.MessageI;

/**
 * Interface <code>ReceptionImplementationI</code>
 * all methods for management of reception message
 * 
 * @author GROUP LAMA
 *
 */
public interface ReceptionImplementationI {
	public void acceptMessage(MessageI m)throws Exception;
	public void acceptMessages(MessageI[] ms)throws Exception;
}
