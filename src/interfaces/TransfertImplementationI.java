package interfaces;

import java.rmi.Remote;

import annexes.message.interfaces.MessageI;


/**
 * Interface <code>PublicationsImplementationI</code>
 * all methods for management of transfer message
 * 
 * @author GROUP LAMA
 *
 */
public interface TransfertImplementationI extends Remote{
	//Transfert un message d'un broker à un autre (Multi JVM)
	public void transfererMessage(MessageI msg, String topic)throws Exception;
}
