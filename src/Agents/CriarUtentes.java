package Agents;



import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CriarUtentes extends Agent{
		
		private Utente utentes;

		protected void setup() {
			super.setup();		
			jade.core.Runtime runtime = jade.core.Runtime.instance();

			Profile profile = new ProfileImpl();
			profile.setParameter(Profile.CONTAINER_NAME, "Utentes");
			profile.setParameter(Profile.LOCAL_HOST, "localhost");
			profile.setParameter(Profile.LOCAL_PORT, "localport");
			
			
			ContainerController container = runtime.createAgentContainer(profile);
			
			
			for(int i=0; i<3;i++){				
				
				AgentController novoUtente; 
				
			    try {
			    	novoUtente = container.createNewAgent("Utente_" + i, Utente.class.getName(), null); 
			    	novoUtente.start(); 
				} catch (StaleProxyException e) {
					System.out.println("Nao conseguiu criar o agente");
				}
			    
			    try {
			    	   Thread.sleep(1000);
			    	} catch (Exception e) {
			    	   e.printStackTrace();
			    	}
			    
			}
	}

		private void block(int i) {
			// TODO Auto-generated method stub
			
		}

	}

	

