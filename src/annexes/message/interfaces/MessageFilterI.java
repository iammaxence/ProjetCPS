package annexes.message.interfaces;

@FunctionalInterface
public interface MessageFilterI {
	public boolean filter(MessageI m);
}
