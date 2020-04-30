package interfaces;


import annexes.message.interfaces.MessageI;
import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;

public interface TransfertImplementationI extends RequiredI, OfferedI{
	
	//Transfert un message d'un broker à un autre (Multi JVM)
	public void transfererMessage(MessageI msg,String topic)throws Exception;
}
