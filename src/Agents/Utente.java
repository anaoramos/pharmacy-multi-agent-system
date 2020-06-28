package Agents;

import java.util.ArrayList;
import java.util.Random;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Utente extends Agent{
	

		private static final long serialVersionUID = 1L;		
		private Agent utente = this;		
		private static int minAgent = 3;		
	    private static int maxAgent = 6;	    
	    private ArrayList<MedicamentosU> MediUtente;	   	    
	    int retirar;	    
	    int tempo;	    
	    int minimo = 1;	    
	    int maximo = 30;	    
	    int num =10; 
	    private ArrayList<String>ListEsp = new ArrayList<String>();	    
	    private ArrayList<String>ListQt = new ArrayList<String>();
	    
	    
	    @Override
		protected void setup() {
	    	
			super.setup();
			
			Random random3 = new Random();			
			retirar=random3.nextInt(5);
			
			if (retirar==0){
				retirar++;
			}
			
			tempo=random3.nextInt(8000);
			criarMedic (this.getAID());
			addBehaviour(new Prescricao(this,tempo));
		}
		
	   

		 private class Prescricao extends TickerBehaviour
			{
			 	public Prescricao(Agent a, long period) {
					super(a, period);
				}

				@Override	
				protected void onTick() {

					try {
				    	   Thread.sleep(1000);
				    	} catch (Exception e) {
				    	   e.printStackTrace();
				    	};
					
					Random random4 = new Random();
					int j = random4.nextInt(num);
					String medicamentoU = "M_"+j;
					for (int i = 0; i < MediUtente.size(); i++)	{
						String nome = MediUtente.get(i).getnome();
						if (medicamentoU.equals(nome)) {
							System.out.println("Retirar -> "+ retirar);
							int qtdInicial= MediUtente.get(i).getquantidade();
							int qtdAtual2 =qtdInicial-retirar;
							int a=0;
							if (qtdAtual2 < minimo) {
								for (int w = 0; w <ListEsp.size(); w++)	{
									if (nome.equals(ListEsp.get(w))) {
										System.out.println("Medicamento "+nome+ "já está pendente.");
										a=1;
									}
								}
								if (a!=1) {
									System.out.println("Não tem medicamento suficiente para a toma. Pedir à farmacia");	
									Integer qtPedido = maximo - qtdInicial;
									String pedido = medicamentoU+";"+qtPedido;
									sendmsg("FarmaciaC",pedido,"Utente");
									ListEsp.add(nome);
									ListQt.add(qtPedido.toString());
									
									
									MessageTemplate messageFarmacia = MessageTemplate.MatchConversationId("Pedido");
									ACLMessage messageF = myAgent.receive(messageFarmacia);
									
									
									
									if ( messageF!= null) {
										
						                String[] pedidoF = messageF.getContent().split(";");
						                String nomeM = pedidoF[0];
						                int quantM = Integer.parseInt(pedidoF[1]);
						                for (int w = 0; w <ListEsp.size(); w++)	{
						                	int Qm=Integer.parseInt(ListQt.get(w));
											if ((nomeM.equals(ListEsp.get(w))) && (quantM==Qm)) {
												ListEsp.remove(w);
												ListQt.remove(w);
											}else if ((nomeM.equals(ListEsp.get(w))) && !(quantM==Qm)){
												Integer Rfinal = Qm -quantM;
												ListQt.set(w,Rfinal.toString());
												System.out.println("Ainda faltam "+Rfinal+"unidades do medicamento "+nomeM);
											}

						                }
						                System.out.println("Conteudo" +messageF.getContent());
						                int recebido = Integer.parseInt(pedidoF[1]);
						                int total = qtdInicial + recebido;
						                for (int r=0;r<MediUtente.size();r++) {
						                	String verif = MediUtente.get(r).getnome();
						                	if (verif.equals(nomeM)){
						                		MediUtente.get(r).setquantidade(total);
								                System.out.println("***Atualizao Farmácia " + utente.getLocalName() +"****");
								                System.out.println(
														
														"Agent \t\t: " + utente.getLocalName() +
														"\nMedicamento \t: "+MediUtente.get(r).getnome()+
														"\nAfter Qtd \t: "+ MediUtente.get(r).getquantidade());
												
												System.out.println("\n********************************"); 
						                	}
						                }
						                 
									}else
									
									{
										block();
									}
								}
							}
								
								else {
									int qtdInicialAUx = MediUtente.get(i).getquantidade();
									MediUtente.get(i).setquantidade(qtdAtual2);
									System.out.println("******Atualizacao ( " + utente.getLocalName() + " )*****\n");								
									
									System.out.println(
											
											"Agent \t\t: " + utente.getLocalName() +
											"\nMedicamento \t: "+MediUtente.get(i).getnome()+
											"\nQtd Antes \t: " + qtdInicialAUx +
											"\nQtd Depois \t: "+ MediUtente.get(i).getquantidade());
									
									System.out.println("\n***************************************");
									
							   
								}
								
							}
							
						}
						
					}
				
				}	
		 
	
		 public ArrayList<MedicamentosU> criarMedic (AID agents) {
			 
			 	MediUtente =  new ArrayList<MedicamentosU>();
				 		        		        
		        Random random = new Random();
		        int numberOfItemsToGenerate = (random.nextInt((maxAgent - minAgent) + 1) + minAgent);
		        for (int i = 0; i < numberOfItemsToGenerate ; i++) {
		        		 int w = random.nextInt(10);
		        		 int quantidade = random.nextInt((maxAgent - minAgent) + 1) + minAgent;
				     MedicamentosU Medicamento= new MedicamentosU("M_"+w, quantidade);     
				     MediUtente.add(Medicamento);      
				    }
		        
		        System.out.println("********Stock do Utente -> "+agents.getLocalName()+"***********");
		        for (int h=0; h<MediUtente.size();h++) {
		        	System.out.println("   Nome:       "+MediUtente.get(h).getnome()+"   Qtd:   "+MediUtente.get(h).getquantidade());
		        }
		        System.out.println("**********************************************");
		        return MediUtente;
			 }
		 
		 
		 
		 private void sendmsg(String agentName, String Content, String Id) {
				AID receiver = new AID();
		        receiver.setLocalName(agentName);
		        ACLMessage msgs = new ACLMessage(ACLMessage.INFORM);
		        msgs.addReceiver(receiver);
		        msgs.setConversationId(Id);
		        msgs.setContent(Content);
		        this.send(msgs);
		    }
		 
	}


	

