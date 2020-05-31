package launcher;

import components.Broker;
import components.Publisher;
import components.Subscriber;
import connectors.TransfertConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;

public class DCVM extends AbstractDistributedCVM {
	
		// URI des JVM defini dans le fichier config.xml
		protected static String	JVM_URI_1 = "jvm_1" ;
		protected static String	JVM_URI_2 = "jvm_2" ;
		protected static String	JVM_URI_3 = "jvm_3" ;

		
		// Port URI des Broker pour l'interconnexion
		protected static final String    URIBrokerManagementInboundPortURI1 = "bm1-iport"; //For Publisher
		protected static final String    URIBrokerManagementInboundPortURI2 = "bm2-iport"; //For Subscriber
		protected static final String    URIBrokerPublicationInboundPortURI = "bp1-iport";
		
		protected static final String    URIBrokerManagementInboundPortURI3 = "bm3-iport"; //For Publisher
		protected static final String    URIBrokerManagementInboundPortURI4 = "bm4-iport"; //For Subscriber
		protected static final String    URIBrokerPublicationInboundPortURI1 = "bp2-iport";
		
		protected static final String    URIBrokerManagementInboundPortURI5 = "bm5-iport"; //For Publisher
		protected static final String    URIBrokerManagementInboundPortURI6 = "bm6-iport"; //For Subscriber
		protected static final String    URIBrokerPublicationInboundPortURI2 = "bp3-iport";
		
		public static final String BROKER_COMPONENT_URI_1 = "my-URI-Broker1" ;
		public static final String PUBLISHER_COMPONENT_URI_1 ="my-URI-Publisher1";
		public static final String SUBSCRIBER_COMPONENT_URI_1="my-URI-subscribe1"; 
		
		public static final String BROKER_COMPONENT_URI_2 = "my-URI-Broker2" ;
		public static final String PUBLISHER_COMPONENT_URI_2 ="my-URI-Publisher2";
		public static final String SUBSCRIBER_COMPONENT_URI_2="my-URI-Subscriber2";
		
		public static final String BROKER_COMPONENT_URI_3 = "my-URI-Broker3" ;
		public static final String PUBLISHER_COMPONENT_URI_3 ="my-URI-Publisher3";
		public static final String SUBSCRIBER_COMPONENT_URI_3="my-URI-Subscriber3";
		
		
		// Les ports pour connecter les broker entre eux
		//Broker 1
		protected String URIBrokerOutboundPortURI1 = "oport1" ;
		protected String URIBrokerInboundPortURI1 = "iport1" ;
		
		//Broker 2
		protected String URIBrokerOutboundPortURI2 = "oport2" ;
		protected String URIBrokerInboundPortURI2 = "iport2" ;
		
		//Broker 3
		protected String URIBrokerOutboundPortURI3 = "oport3" ;
		protected String URIBrokerInboundPortURI3 = "iport3" ;
		

		// Les composants qu'on instancie
		protected String brokerURI_1;
		protected String brokerURI_2;
		protected String brokerURI_3;

		protected String publisherURI_1 ;
		protected String publisherURI_2 ;
		protected String publisherURI_3 ;

		protected String subscriberURI_1 ;
		protected String subscriberURI_2 ;
		protected String subscriberURI_3 ;


		public DCVM(String[] args, int xLayout, int yLayout) throws Exception {
			super(args, xLayout, yLayout);
		}
		
		@Override
		public boolean startStandardLifeCycle(long duration) {
			try {
				assert	duration	> 0 ;
				this.deploy() ;
				System.out.println("starting...") ;
				this.start() ;
				
				// on synchronise toute les jvm afin que
				// toutes methodes start de chaque jvm soit termine avant
				// les methodes execute
				this.waitOnCyclicBarrier();
				
				System.out.println("executing...") ;
				this.execute() ;
				Thread.sleep(duration) ;
				System.out.println("finalising...") ;
				this.finalise() ;
				System.out.println("shutting down...") ;
				this.shutdown() ;
				System.out.println("ending...") ;
				return true ;
			} catch (Exception e) {
				e.printStackTrace() ;
				return false ;
			}
		}
		
		@Override
		public void	initialise() throws Exception {
			super.initialise() ;
		}

		@Override
		public void	instantiateAndPublish() throws Exception {
			
			if (thisJVMURI.equals(JVM_URI_1)) {
				


				this.brokerURI_1 = AbstractComponent.createComponent(
									Broker.class.getCanonicalName(),
									new Object[]{1,
									 0,
									 BROKER_COMPONENT_URI_1,
									 URIBrokerOutboundPortURI1,
									 URIBrokerInboundPortURI1}) ; 
				
				this.publisherURI_1 = AbstractComponent.createComponent(
										Publisher.class.getCanonicalName(),
										new Object[]{1,
										0,
										PUBLISHER_COMPONENT_URI_1,
										BROKER_COMPONENT_URI_1}) ;
				
				this.subscriberURI_1 = AbstractComponent.createComponent(
										Subscriber.class.getCanonicalName(),
										new Object[]{1,
										0,
										SUBSCRIBER_COMPONENT_URI_1,
										BROKER_COMPONENT_URI_1}) ;;
				
//				this.addDeployedComponent(uriCourtier_1) ;
//				this.addDeployedComponent(uriProducteur_1) ;
//				this.addDeployedComponent(uriConsommateur_1) ;
				
			} 
			else if (thisJVMURI.equals(JVM_URI_2)) {

					this.brokerURI_2 = AbstractComponent.createComponent(
							Broker.class.getCanonicalName(),
							new Object[]{1,
							 0,
							 BROKER_COMPONENT_URI_2,
							 URIBrokerOutboundPortURI2,
							 URIBrokerInboundPortURI2}) ; 
		
//					this.publisherURI_2 = AbstractComponent.createComponent(
//											Publisher.class.getCanonicalName(),
//											new Object[]{1,
//											0,
//											PUBLISHER_COMPONENT_URI_2,
//											BROKER_COMPONENT_URI_2}) ;
		
					this.subscriberURI_2 = AbstractComponent.createComponent(
											Subscriber.class.getCanonicalName(),
											new Object[]{1,
											0,
											SUBSCRIBER_COMPONENT_URI_2,
											BROKER_COMPONENT_URI_2}) ;
				
			}
//			else if (thisJVMURI.equals(JVM_URI_3)) {
//				
//				this.brokerURI_3 = AbstractComponent.createComponent(
//						Broker.class.getCanonicalName(),
//						new Object[]{1,
//						 0,
//						 BROKER_COMPONENT_URI_3,
//						 URIBrokerOutboundPortURI3,
//						 URIBrokerInboundPortURI3}) ; 
//	
//				this.publisherURI_3 = AbstractComponent.createComponent(
//										Publisher.class.getCanonicalName(),
//										new Object[]{1,
//										0,
//										PUBLISHER_COMPONENT_URI_3,
//										BROKER_COMPONENT_URI_3}) ;
//				
//				this.subscriberURI_3 = AbstractComponent.createComponent(
//										Subscriber.class.getCanonicalName(),
//										new Object[]{1,
//										0,
//										SUBSCRIBER_COMPONENT_URI_3,
//										BROKER_COMPONENT_URI_3}) ;;
//				
//
//			}
			else {
				System.out.println("Unknown JVM URI... " + thisJVMURI) ;
			}
			
			super.instantiateAndPublish();
		}

		
		@Override
		public void	interconnect() throws Exception {
			//Connexion des brokers entre eux
			
			
			if (thisJVMURI.equals(JVM_URI_1)) {
				this.doPortConnection(brokerURI_1,URIBrokerOutboundPortURI1 , URIBrokerInboundPortURI2, TransfertConnector.class.getCanonicalName());
			}
			else if (thisJVMURI.equals(JVM_URI_2)) {
				
				this.doPortConnection(brokerURI_2, URIBrokerOutboundPortURI2, URIBrokerInboundPortURI1, TransfertConnector.class.getCanonicalName());

			} 
//			else if (thisJVMURI.equals(JVM_URI_3)) {
//				
//				this.doPortConnection(brokerURI_3, URIBrokerOutboundPortURI3, URIBrokerInboundPortURI1, TransfertConnector.class.getCanonicalName());
//
//			} else {
//				System.out.println("Unknown JVM URI... " + thisJVMURI) ;
//			}
			super.interconnect();
		}


		@Override
		public void	shutdown() throws Exception {
			super.shutdown();
		}

		public static void main(String[] args) {
			try {
				DCVM da  = new DCVM(args, 2, 5);
				da.startStandardLifeCycle(15000L);
				Thread.sleep(10000L);
				System.exit(0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

}
