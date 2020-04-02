package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import plugins.PublisherPlugin;
import annexes.Chrono;
import annexes.message.Message;
import annexes.message.Properties;
import annexes.message.TimeStamp;
import annexes.message.interfaces.MessageI;

/**
 * 
 * @author GROUP LAMA
 *
 */
@RequiredInterfaces(required = {ManagementCI.class, PublicationCI.class})
public class Publisher 
extends AbstractComponent {
	
	protected final static String mypluginURI="publisherPlugin";
	protected PublisherPlugin myplugin;
	
	/**
	 * Constructor of Publisher Component
	 * @param nbThreads is the number of thread s
	 * @param nbSchedulableThreads is the number of schedulable threads
	 * @param uri of the Component
	 * @throws Exception
	 */
	protected Publisher(int nbThreads, int nbSchedulableThreads, String uri,String URI_BROKER) throws Exception{
		super(uri, nbThreads, nbSchedulableThreads);
		assert uri != null;
		
		/**----------- PLUGIN INSTALLATION ------------**/
		myplugin=new PublisherPlugin(URI_BROKER);
		myplugin.setPluginURI(mypluginURI);
		this.installPlugin(myplugin);
		assert	this.isInstalled(mypluginURI);
		
		/**----------------- TRACE --------------------**/
		this.tracer.setTitle(uri) ;
		this.tracer.setRelativePosition(0, 2) ;
		this.toggleTracing() ;
	}
	
	
	/**-----------------------------------------------------
	 * -------------------- LIFE CYCLE ---------------------
	 ------------------------------------------------------*/
	@Override
	public void	start() throws ComponentStartException {
		super.start() ;
		this.logMessage("starting Publisher component.") ;
	}
	
	@Override
	public void	execute() throws Exception{
		super.execute();
		
		/**------------------- Scenarios in order to tests of the methods ---------------**/
		try {
			MessageI[] messages = {new Message("Mon 1er message."), new Message("Mon second message.")};
			String [] topics = {"Peche&Cuisine", "CPS"};
			
			/**
			 * Choose scenario that you want (1 to 5):
			 */
			int[] scenario = {2, 3, 4, 5};
			
			Chrono chrono=new Chrono(); //Chrono permet la preuve que les thread améliore le temps de calcul
			chrono.start();
			for(int i=0; i<scenario.length; i++) {
				switch (scenario[i]) {
					case 1: /** Scenario 1: Publication of 10 000 in the topic Peche&Cuisine**/
						this.logMessage("Publisher publit des messages dans Peche&Cuisine");
						for(int y=0; y<10000; y++) {
							myplugin.publish( new Message("Le saumon c'est trop bon!"), "Peche&Cuisine"); }
						break;
						
					case 2: /** Scenario 2: Publish a message in 2 topics then multiple messages in those topics**/
						this.logMessage("Publisher publit un message dans Peche&Cuisine et CPS");
						myplugin.publish(new Message("Hello World!"), topics);
						this.logMessage("Publisher publit des messages Peche&Cuisine et CPS");
						myplugin.publish(messages, topics);
						break;
						
					case 3: /** Scenario 3: Create the topic Automobile and publish messages **/
						this.logMessage("Publisher creer le topic Automobile");
						myplugin.createTopic("Automobile");
						this.logMessage("Publisher publit des messages dans Automobile");
						myplugin.publish(messages, "Automobile");
						break;
						
					case 4: /** Scenario 4: destroy a topic  **/
						this.logMessage("Demande de suppresion du topic: Automobile");
						myplugin.destroyTopic("Automobile");;
						break;
						
					case 5: /** Scenario 5: Publish a message with properties in  Peche&Cuisine **/
						this.logMessage("Publisher publit des messages dans Peche&Cuisine");
						Properties props = new Properties();
						props.putProp("thon", true);
						Message msg = new Message("Non! Le thon c'est meilleur!", new TimeStamp(), props );
						myplugin.publish(msg, "Peche&Cuisine");
						break;
					case 6: /** Scenario 6: Test multi-component  **/
                        this.logMessage("Publisher publit un message dans Test");
                        myplugin.publish(new Message("Hello World!"), "Test");
                        break;
						
					default: break;	
				}
			}
			chrono.stop();
			this.logMessage("Chrono : "+chrono.getDureeMs()); 
		} catch (Exception e) { throw new RuntimeException(e) ; }
		/**------------------------------ End of Scenarios -----------------------------**/
		
		
	}
	
	@Override
	public void	finalise() throws Exception{
		this.logMessage("stopping publisher component.") ;
		this.printExecutionLogOnFile("publisher");
		
		super.finalise();
	}

}
