package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.PublicationCI;
import annexes.message.interfaces.MessageI;

public class PublicationCInBoundPort 
extends AbstractInboundPort implements PublicationCI{
	private static final long serialVersionUID = 1L;

	public PublicationCInBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
		assert	uri != null && owner instanceof Broker ;
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).publish(m, topic); return null;}) ;
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).publish(m, topics); return null;}) ;
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).publish(ms, topic); return null;}) ;
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).publish(ms, topics); return null;}) ;
		
	}

}
