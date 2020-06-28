package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.Random;


public class FarmaciaC extends Agent{

	private static final long serialVersionUID = 1L;	
	int numeroAgents=3;	
	int minimo = 5;	
	int maximo = 150;	
	int tempoMax=20;
	String [] medDispo = new String [11];	
	String [] MedQuant = new String [11];	


	//Arrays Necessarios 
	private ArrayList<String> ListEsp = new ArrayList<String>(); //Nomes dos medicamentos em espera
    private ArrayList<String> ListQt = new ArrayList<String>(); //Quantidade dos medicamentos em espera
    private ArrayList<String> ListAgent = new ArrayList<String>(); //Nome dos utentes que estão em espera de medicamento
    private ArrayList<String> MelhorForn = new ArrayList<String>(); //Nomes dos fornecedores em negociação
    private ArrayList<String> MelhorPreco = new ArrayList<String>(); // Preços oferecidos pelos fornecedores em negociação
	private ArrayList<String> ListMed = new ArrayList<String>(); // Medicamentos que estão a ser negociados
	private ArrayList<String> MelhorQuant = new ArrayList<String>(); //Quantidades dos fornecedores em negociação
	private ArrayList<String> MelhorTempo = new ArrayList<String>(); //tempos de entrega dos fornecedores em negociação
    
    int NumFor=3; //Numero de fornecedores	
	private Agent FarmaciaC=this;	
	int a=0;	
	int numeroMedic=10; //numero de medicamentos
	
		@Override
		protected void setup() {
			System.out.println("\n*****************************************");  
			System.out.println("*****************Farmácia****************");
			System.out.println("");
			System.out.println("*************Stock da Farmácia***********");
			System.out.println("");
			System.out.println(
					
					"\tMedicamento: "+
					"\tQuantidade: ");
			
			System.out.println("\t"+
					"\t");
			for (int i = 0; i <medDispo.length ; i++) {
				Random random = new Random();
				Integer quantidade = random.nextInt(100);
				medDispo[i]="M_"+i;
				MedQuant[i]=quantidade.toString();
				

	            System.out.println(
						
						"\t"+medDispo[i]+
						"\t\t"+MedQuant[i]);

			}
			System.out.println("\n*****************************************");  

			addBehaviour(new RespostaNegoc ());
			addBehaviour(new verificarStock(this,6000));

		}

				
		
		private class RespostaNegoc extends CyclicBehaviour {

			private static final long serialVersionUID = 1L;

			@Override
	        public void action() {
	        		

	            block(6000);
	            
	            MessageTemplate messageTemplate_r1 = MessageTemplate.MatchConversationId("Resposta1");
				ACLMessage message_r1 = myAgent.receive(messageTemplate_r1);
				
				MessageTemplate messageUtente = MessageTemplate.MatchConversationId("Utente");
				
				ACLMessage messageU = myAgent.receive(messageUtente);
				
				MessageTemplate messageTemplate_f1 = MessageTemplate.MatchConversationId("questionaForn");
				ACLMessage message_f1 = myAgent.receive(messageTemplate_f1);
	
		        
	            if (message_r1 != null ) {
		           
	            	System.out.println("*********************************************");
	            	
	            	System.out.println("Recebi resposta ao meu pedido...");
	                System.out.print("A mensagem vem do: " + message_r1.getSender().getLocalName()+ "e o conteudo é "+message_r1.getContent());
	                
	                System.out.println("*********************************************");
	                
	                
	                String[] pedido = message_r1.getContent().split(";");
	                String remetente = message_r1.getSender().getLocalName();
	                String nome = pedido[0];
	                Integer tempo = Integer.parseInt(pedido[3]);
	                String enviar = nome+";"+pedido[1];
	                Integer quantidade = Integer.parseInt(pedido[1]);
	                Integer preco = Integer.parseInt(pedido[2]);
	                
	                MelhorForn.add(remetente);
	                MelhorPreco.add(preco.toString());
	                MelhorQuant.add(quantidade.toString());
	                MelhorTempo.add(tempo.toString());
	                
	                
	                if(MelhorForn.size()==NumFor) {
	                	int bestprice=100000;
	                	int p = 0;
	                	int t =0;
	                	for (int i=0; i<MelhorForn.size();i++) {
	                		int mp =Integer.parseInt(MelhorPreco.get(i));
	                		int mt= Integer.parseInt(MelhorTempo.get(i));
	                		if((mp<bestprice) && (mt<tempoMax)) {
	                			bestprice=mp;
	                			p=i;
	                			t=mt;
	                		}
	                	}
	                	if (bestprice == 100000) {
	                		System.out.println("Nenhum fornecedor possui o medicamento pedido em negociação");
	                	} 
	                	
	                	else {
	                		Integer QtMelhor = Integer.parseInt(MelhorQuant.get(p));
	                		String mess = nome+";"+QtMelhor;
		                	String fornMelhor = MelhorForn.get(p);
		              
		                	
		                	System.out.println("O melhor fornecedor é "+fornMelhor+ "e o seu preço é "+bestprice+ "e o tempo de entrega é "+t);
		                	
		                	sendmsg(fornMelhor,mess,"AceiteiNeg");
		                	AumentaStock(nome,QtMelhor);
		                	
		                	MelhorForn.remove(p);
			                MelhorPreco.remove(p);
			                MelhorQuant.remove(p);
			                MelhorTempo.remove(p);
			                
			               
			                for (int k = 0; k<ListMed.size();k++) {
			                	String me = ListMed.get(k);
			                	if(nome.equals(me)) {
			                		ListMed.remove(k);
			                	}
			                }

			                System.out.println("*********Atualizacao**********");
				    		System.out.println(
									
									"\tMedicamento: "+
									"\tQtd Final: ");
				    		for (int i = 0; i < medDispo.length ; i++) {
				    			if (nome.equals(medDispo[i])) {
				    				System.out.println(
					        						"\t"+medDispo[i]+
					        						"\t\t"+MedQuant[i]);
				    			}
				    		}

							System.out.println("\n********************************");  
			                
			                
			                for (int i=0; i<MelhorForn.size();i++) {
			                	String forn= MelhorForn.get(i);
			                	sendmsg(forn,"Nao aceitei a proposta","terminarNeg");
		                		}
		                	
			                MelhorForn.clear();
			                MelhorPreco.clear();
			                MelhorQuant.clear();
			                MelhorTempo.clear();
		                	
		                }
                	}
	                	
      
    		}

	            if (messageU != null) {
					
	            	System.out.println("**************Mensagem do "+messageU.getSender().getLocalName()+"******************");
		            System.out.println();
		            String agenteU=messageU.getSender().getLocalName();
		            String[] pedido = messageU.getContent().split(";");
		            String nome = pedido[0];
		            Integer quant = Integer.parseInt(pedido[1]);
		            System.out.println("Pretende adquirir o Medicamento: " + pedido[0]+ " Com a quantidade: "+pedido[1]);
		            
		            for (int j = 0; j < medDispo.length; j++)	{
		            	int qtStock = Integer.parseInt(MedQuant[j]);
		        		if ((nome.equals(medDispo[j])) && (qtStock>=quant)){
		        			retirarStock(nome,quant);
		        			String EnviUt = nome+";"+pedido[1];
		        			System.out.println("Pedido enviado ");
		        			System.out.println("**********************************");
		        			sendmsg(agenteU,EnviUt,"Pedido");
		        			
		        		}
		        		else if ((nome.equals(medDispo[j])) && (qtStock>0) && (qtStock<quant) ){
		        			Integer diferenca =  quant - qtStock; //quantidade que ficou pendente enviar
		        			String EnviUt1 = nome+";"+qtStock;
		        			System.out.println("antes:" +MedQuant[j]);
		        			retirarStock(nome,qtStock);
		        			System.out.println("depois:" +MedQuant[j]);
		        			sendmsg(agenteU,EnviUt1,"Pedido");
							ListAgent.add(agenteU);
							ListEsp.add(nome);
							ListQt.add(diferenca.toString());}
		            
						
		        			else if ((nome.equals(medDispo[j])) && (qtStock==0) && (qtStock<quant) ){				
								ListAgent.add(agenteU);
								ListEsp.add(nome);
								ListQt.add(quant.toString());
							
								}
	            }
		        			
    		}
		
		            

	            if (message_f1 != null) {
                    
                	System.out.print("A mensagem vem do: " + message_f1.getSender().getLocalName());

		            String agenteU=message_f1.getSender().getLocalName();
		            String[] pedido = message_f1.getContent().split(";");
		            String remetente = message_f1.getSender().getLocalName();
		            String nome = pedido[0];
		            int tempo = Integer.parseInt(pedido[2]);
		            int quant = Integer.parseInt(pedido[1]);
		            int preco = Integer.parseInt(pedido[3]);
		            
		            
		            for (int i = 0; i < MedQuant.length; i++)	{
						int qtdInicial = Integer.parseInt(MedQuant[i]);
						if ((tempo<tempoMax) && (nome.equals(medDispo[i])) && (qtdInicial < minimo) && (preco<4)){
							
						int b = 0;
	                	for (int w = 0; w <ListEsp.size(); w++)	{
							if (nome.equals(ListEsp.get(w))) {
								System.out.println("Medicamento "+nome+ "já está pendente.");
								sendmsg(remetente,"Não estou interessada","terminarNeg2");
								b=1;
							}
	                	}
	                	
						if (b!=1) {
							System.out.println("Aceito a proposta");	
							if ((quant+qtdInicial)<maximo) {
								String Envpedido = nome+";"+quant;
								AumentaStock(nome,quant);
								sendmsg(remetente,Envpedido,"AceiteiNeg");
								System.out.println("Aceitei a quantidade "+quant+"do medicamento "+nome+ "que será entregue em mais "+tempo+"dias");
							}
							else {
								int quantPed = maximo-qtdInicial;
								String Envpedido2 = nome+";"+quantPed;
								AumentaStock(nome,quant);
								sendmsg(remetente,Envpedido2,"AceiteiNeg");	
								System.out.println("Aceitei apenas a quantidade "+quantPed+"do valor "+quant+"do medicamento "+nome);
							}
	
						}}
						else if (nome.equals(medDispo[i])){
							sendmsg(remetente,"Não estou interessada","terminarNeg2");
							System.out.println("\tNao preciso do medicamento pedido");
						}
		            }
	            }
			
			}
		}
		
		public void retirarStock (String nome, int qt ) {
			for (int j = 0; j < medDispo.length; j++)	{
        		if ((medDispo[j].equals(nome))){
        			String inicial = MedQuant[j];
        			Integer total = Integer.parseInt(inicial)-qt;
        			MedQuant[j]=total.toString();
        			System.out.println("Medicamento: "+ nome+ " Quantidade Inicial:  "+ inicial+ "Quantidade Final:  "+MedQuant[j]);
        		}
        	
        }
		}
		
		public void AumentaStock (String nome, Integer qt ) {
			Integer total =0;
			for (int j = 0; j < medDispo.length; j++)	{
        		if ((medDispo[j].equals(nome))){
        			String inicial = MedQuant[j];
        			total = Integer.parseInt(inicial)+qt;
        			MedQuant[j]=total.toString();
        			System.out.println("Lista ->"+ListEsp+ "nome -> "+nome);
        			for(int h=0;h<ListEsp.size();h++) {
        				String nomeMed = ListEsp.get(h);
        				int qtM = Integer.parseInt(ListQt.get(h));
        				if ((nomeMed.equals(nome)) && (qtM<=total)) {
        					String Enviar = nome+";"+qtM;
        					String remetente = ListAgent.get(h);
        					sendmsg(remetente,Enviar,"Pedido");
        					System.out.println("Enviou o que faltava do medicamento "+nomeMed+"para o remetente "+remetente);
        					retirarStock(nome,qtM);
        					
        				}
        			}
        			System.out.println("Medicamento: "+ nome+ "Quantidade "+MedQuant[j]);
        		}
        	
        }
			
		}
		
			private void sendmsg(String agentName, String Content, String Id) {
			                AID receiver = new AID();
			                receiver.setLocalName(agentName);
			                ACLMessage msgs = new ACLMessage(ACLMessage.INFORM);
			                msgs.addReceiver(receiver);
			    	        msgs.setConversationId(Id);
			                msgs.setContent(Content);
			                FarmaciaC.send(msgs);
                       		}
			

			private void block(int i) {
				// TODO Auto-generated method stub
				
			}
			
			 private class verificarStock extends TickerBehaviour
				{
				 	public verificarStock(Agent a, long period) {
						super(a, period);
					}

					@Override	
					protected void onTick() {

						try {
					    	   Thread.sleep(1000);
					    	} catch (Exception e) {
					    	   e.printStackTrace();
					    	};
						
						
						for (int i = 0; i < MedQuant.length; i++)	{
							int qtdInicial= Integer.parseInt(MedQuant[i]);
							if (qtdInicial < minimo) {
								String nome = medDispo[i];
								int pedido = 75;
								int u =0;
								for (int k=0;k<ListEsp.size();k++) {
									if (nome.equals(ListEsp.get(k))){
										pedido=pedido+Integer.parseInt(ListQt.get(k));
									}
								}
								for (int k=0;k<ListMed.size();k++) {
									if (nome.equals(ListMed.get(k))){
										u=1;	
									}
								}
								
								if (u!=1) {
									String message = nome+";"+pedido;
									ListMed.add(nome);
									for (int j=0; j<numeroAgents;j++){
					    				System.out.println("Enviar negociação "+ "fornecedor_"+j);				    				
					    				sendmsg("Fornecedor_" + j,message,"Inicio");		
				    			}
							}
								
						}
					}
				}
			}	
    }
			  



           	
                       		
                       		
                       		
                     
                       	
		
		
		


			
			
			
