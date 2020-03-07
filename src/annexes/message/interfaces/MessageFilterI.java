package annexes.message.interfaces;

/**
 * 
 * @author Group LAMA
 *
 */
@FunctionalInterface
public interface MessageFilterI {
	public boolean filter(MessageI m);
}
