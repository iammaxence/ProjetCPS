package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import annexes.Client;
import annexes.GestionClient;

/**
 * This JUnit class test all the 
 * method of the class GestionClient
 * (in annex)
 * 
 * @author Group LAMA
 *
 */
public class GestionClientTest {
	
	protected static GestionClient gc;
	protected static Client client;
	protected final static String uriClient = "inBoundPortTest";
	
	@BeforeClass
	public static void initialise() throws Exception {
		gc = new GestionClient();
//		client = new Client(uriClient, new ReceptionCOutBoundPort(null));
	}

	/**
	 * Test impossible a réalisé car neccesite un ComposantI pour
	 * le port out de reception et n'admet pas le null
	 */
	@Test
	public void testClient() {
		assertFalse(gc.isClient(uriClient));
//		gc.addClient(uriClient, client);
//		assertTrue(gc.isClient(uriClient));
	}
	
	/**
	 * Etant donnée que le Client n'a pas été rajouter au préalable
	 * il n'est pas possible réaliser une subscription d'un client
	 * non connu
	 */
	@Test
	public void testSubscription() {
		assertFalse(gc.isSubscribe("test", uriClient));
		assertFalse(gc.addSouscription("test", uriClient, null));
		//assertTrue(gc.isSubscribe("test", uriClient));
	}

}
