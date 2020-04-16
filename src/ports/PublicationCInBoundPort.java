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
	 * @param owner of the port
	 * @param indexPool is the index of pool thread for the handleRequest
	 * @throws Exception
	 */
	public PublicationCInBoundPort(ComponentI owner, int indexPool) throws Exception {
		super(PublicationCI.class, owner);
		assert owner instanceof Broker ;
		this.indexPool=indexPool;
	}
	
	/**
	 * Constructor of PublicationCInBoundPort
	 * @param owner of the port
	 * @throws Exception
	 */
	public PublicationCInBoundPort(ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
		assert owner instanceof Broker ;
		this.indexPool=-1;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		if (indexPool != -1) {
			this.getOwner().handleRequestSync(indexPool,
					owner -> {((Broker)owner).publish(m, topic); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(owner -> {((Broker)owner).publish(m, topic); return null;}) ;
		}
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		if (indexPool != -1) {
			this.getOwner().handleRequestSync(indexPool,
						owner -> {((Broker)owner).publish(m, topics); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(owner -> {((Broker)owner).publish(m, topics); return null;}) ;
		}
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		if (indexPool != -1) {
			this.getOwner().handleRequestSync(indexPool,
						owner -> {((Broker)owner).publish(ms, topic); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(owner -> {((Broker)owner).publish(ms, topic); return null;}) ;
		}
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		if (indexPool != -1) {
			this.getOwner().handleRequestSync(indexPool,
						owner -> {((Broker)owner).publish(ms, topics); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(owner -> {((Broker)owner).publish(ms, topics); return null;}) ;
		}
		
	}

}
