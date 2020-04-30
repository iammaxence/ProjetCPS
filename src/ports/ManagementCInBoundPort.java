package ports;

import components.Broker;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;
import annexes.message.interfaces.MessageFilterI;

/**
 * This class <code>ManagementCInBoundPort</code>
 * represent the in bound port that implement 
 * ManagementCI 
 * 
 * @author GROUP LAMA
 *
 */
public class ManagementCInBoundPort
extends AbstractInboundPort implements ManagementCI {
	
	private static final long serialVersionUID = 1L;
	private int indexPool=-1; 
	
	/**
	 * Constructor of ManagementCInBoundPort
	 * 
	 * pre owner instanceof Broker
	 * 
	 * @param owner of this port
	 * @throws Exception
	 */
	public ManagementCInBoundPort(ComponentI owner) throws Exception {
		super(ManagementCI.class, owner);
		assert owner instanceof Broker;
	}
	
	/**
	 * Constructor of ManagementCInBoundPort
	 *	
	 * pre owner instanceof Broker
	 * 
	 * @param owner of this port
	 * @param indexPool : index that defined the pool thread for the handleRequest
	 * @throws Exception
	 */
	public ManagementCInBoundPort(ComponentI owner, int indexPool) throws Exception {
		super( ManagementCI.class, owner);
		assert owner instanceof Broker;
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
	
	//Ici pas de Async car on attends un resultat
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
