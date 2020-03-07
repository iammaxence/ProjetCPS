package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.PublicationCI;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
public class PublicationCInBoundPort 
extends AbstractInboundPort implements PublicationCI{
	
	private static final long serialVersionUID = 1L;
	private int indexPool;

	/**
	 * Constructor of PublicationCInBoundPort
	 * @param uri of the port
	 * @param owner of the port
	 * @param indexPool is the index of pool thread
	 * @throws Exception
	 */
	public PublicationCInBoundPort(String uri, ComponentI owner, int indexPool) throws Exception {
		super(uri, PublicationCI.class, owner);
		assert	uri != null && owner instanceof Broker ;
		this.indexPool=indexPool;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.getOwner().handleRequestAsync(indexPool,
					owner -> {((Broker)owner).publish(m, topic); return null;}) ;
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.getOwner().handleRequestAsync(indexPool,
					owner -> {((Broker)owner).publish(m, topics); return null;}) ;
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.getOwner().handleRequestAsync(indexPool,
					owner -> {((Broker)owner).publish(ms, topic); return null;}) ;
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.getOwner().handleRequestAsync(indexPool,
					owner -> {((Broker)owner).publish(ms, topics); return null;}) ;
		
	}

}
