package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;
import annexes.message.interfaces.MessageFilterI;

public class ManagementCInBoundPort
extends AbstractInboundPort implements ManagementCI {
	private static final long serialVersionUID = 1L;
	private int indexPool=-1; 
	
	public ManagementCInBoundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
		assert	uri != null && owner instanceof Broker;
	}
	
	
	public ManagementCInBoundPort(String uri, ComponentI owner, int indexPool) throws Exception {
		super(uri, ManagementCI.class, owner);
		assert	uri != null && owner instanceof Broker;
		this.indexPool=indexPool;
	}
	
	
	@Override
	public void subscribe(String topic, String inboundPortUri) throws Exception {
		if(indexPool == -1) {
			this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).subscribe(topic, inboundPortUri); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(indexPool,
					owner -> {((Broker)owner).subscribe(topic, inboundPortUri); return null;}) ;
		}
		
	}

	@Override
	public void subscribe(String[] topics, String inboundPortUri) throws Exception {
		if(indexPool == -1) {
			this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).subscribe(topics, inboundPortUri); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(indexPool,
					owner -> {((Broker)owner).subscribe(topics, inboundPortUri); return null;}) ;
		}
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboundPortUri) throws Exception {
		if(indexPool == -1) {
			this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).subscribe(topic, filter, inboundPortUri); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(indexPool,
					owner -> {((Broker)owner).subscribe(topic, filter, inboundPortUri); return null;}) ;
		}
		
	}

	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.getOwner().handleRequestSync(
				owner -> {((Broker)owner).modifyFilter(topic, newFilter, inboundPortUri); return null;}) ;
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		if(indexPool == -1) {
			this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).unsubscribe(topic, inboundPortURI); return null;}) ;
		}else {
			this.getOwner().handleRequestSync(indexPool,
					owner -> {((Broker)owner).unsubscribe(topic, inboundPortURI); return null;}) ;
		}
		
	}

	@Override
	public void createTopic(String topic) throws Exception {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).createTopic(topic); return null;}) ;
		
	}

	@Override
	public void createTopics(String[] topics)throws Exception  {
		this.getOwner().handleRequestSync(
					owner -> {((Broker)owner).createTopics(topics); return null;}) ;
		
	}

	@Override
	public void destroyTopic(String topic) throws Exception  {
		this.getOwner().handleRequestSync(
				owner ->{ ((Broker)owner).destroyTopic(topic); return null;}) ;
	}

	@Override
	public boolean isTopic(String topic) throws Exception { 
		return this.getOwner().handleRequestSync(
					owner -> ((Broker)owner).isTopic(topic)) ;
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.getOwner().handleRequestSync(
					owner -> ((Broker)owner).getTopics()) ;
	}

}
