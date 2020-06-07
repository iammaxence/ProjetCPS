package annexes.message.interfaces;

/**
 * Functionnal Interface
 * of MessageFilter
 * 
 * @author Group LAMA
 *
 */
@FunctionalInterface
public interface MessageFilterI {
	public boolean filter(MessageI m);
}
