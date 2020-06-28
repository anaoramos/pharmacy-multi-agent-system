package Agents;


import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CriarFornecedores extends Agent{
		
		private Fornecedor fornecedor;
		
		private static int MIN_PER_AGENT = 3;
	    private static int MAX_PER_AGENT = 6;
	    private static int MIN_ITEM_VALUE = 200;
	    private static int MAX_ITEM_VALUE = 1000;
		
		static ContainerController container;
		
		Agent criarforn = this;


		protected void setup() {
			super.setup();		
			jade.core.Runtime runtime = jade.core.Runtime.instance();
			

			Profile profile = new ProfileImpl();
			profile.setParameter(Profile.CONTAINER_NAME, "Fornecedores");
			profile.setParameter(Profile.LOCAL_HOST, "localhost");
			profile.setParameter(Profile.LOCAL_PORT, "localport");
			container = runtime.createAgentContainer(profile);
			
			for(int i=0; i<3;i++){
				
				AgentController novoFornecedor;
				
			    try {
			    	
			    	novoFornecedor = container.createNewAgent("Fornecedor_" + i, Fornecedor.class.getName(), null); 
			    block(10000);
			    	novoFornecedor.start(); 
				} 
			    catch (StaleProxyException e) {
					e.printStackTrace();
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






	

