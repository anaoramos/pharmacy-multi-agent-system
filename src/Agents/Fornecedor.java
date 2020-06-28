package Agents;



import java.util.Random;
import java.util.ArrayList;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import Agents.MedicamentosForn;

public class Fornecedor extends Agent{

		private Agent fornecedor = this;
		private ArrayList<MedicamentosForn> MedList;
		private static int minAgent = 3;
		private static int maxAgent = 6;
	    int melhor;
	    int tempo;
		
		@Override
		protected void setup() {
			
			super.setup();

			criarMedic (this.getAID());
	        Random random3 = new Random();
	        tempo=random3.nextInt(80000);
		    melhor=0;
	        this.addBehaviour(new RespostaNeg());	        
	        this.addBehaviour(new Questionar(this, 80000));	
	        }
		
		
		
		private class RespostaNeg extends CyclicBehaviour {
			
			@Override
			public void action() {
				
				
				MessageTemplate messageTemplate_inicio = MessageTemplate.MatchConversationId("Inicio");
				ACLMessage message = myAgent.receive(messageTemplate_inicio);
				
				MessageTemplate messageTemplate_r1 = MessageTemplate.MatchConversationId("Resposta1");
				ACLMessage message_r1 = myAgent.receive(messageTemplate_r1);
				
				MessageTemplate messageTemplate_r2 = MessageTemplate.MatchConversationId("terminarNeg");
				ACLMessage message_r2 = myAgent.receive(messageTemplate_r2);
				
				MessageTemplate messageTemplate_r3 = MessageTemplate.MatchConversationId("AceiteiNeg");
				ACLMessage message_r3 = myAgent.receive(messageTemplate_r3);
				
				MessageTemplate messageTemplate_termina2 = MessageTemplate.MatchConversationId("terminarNeg2");
				ACLMessage message_r4 = myAgent.receive(messageTemplate_termina2);
				

		        //ID "Inicio:
	            if (message != null ) {
	            		
	            	RandomPreco();
	            	RandomTempo();
	            	System.out.println("***************************************************");
	            	System.out.println("Recebi Pedido de negociação da farmácia");
	                System.out.print("Conteudo : "+message.getContent());

	                
	                String[] pedido = message.getContent().split(";");
	                String nome = pedido[0];
	                Integer quantidade = Integer.parseInt(pedido[1]);
	                System.out.println("A farmácia está a pedir unidades:    "+ quantidade+ "do medicamento "+nome);
	
	                String k=fornecedor.getLocalName();
	                int possuiStock= verificarStock(nome,quantidade);
	                if (possuiStock == 1) {
	            		for (int m = 0; m < MedList.size(); m++)	{
	            			if (nome.equals(MedList.get(m).getnome())) {
	            				int p=MedList.get(m).getpreco();
								int q=MedList.get(m).getquantidade();
								int tempo = MedList.get(m).gettempo();
								String r1=nome+";"+quantidade+";"+p+";"+tempo;
								sendmsg("FarmaciaC", r1, "Resposta1");
	            			}
							
						}
	                	
	                }
	                else {
	                	System.out.println("Não fiz a melhor proposta");
	                	String r2=nome+";"+"0"+";"+"100000"+";"+"40"; //Mesmos os que nao tem, estão a  aneviar preços nao competitivos
	                	sendmsg("FarmaciaC", r2, "Resposta1");
	                }
        		}	
            		//ID terminarNeg:
		            if (message_r2 != null ) {
	            		System.out.println("A farmácia não aceitou a proposta...");
	            		}
		            
		            //ID AceiteiNeg:
		            if (message_r3 != null ) {
	            		System.out.println("A farmácia aceitou a proposta.");
	            		String[] receber = message_r3.getContent().split(";");
	            		String nome = receber[0];
	            		String qt = receber[1];
	
	            		int quantidade = Integer.parseInt(qt);
	            		retirarStock(nome, quantidade);
	            		
	        			}

		            if (message_r4 != null ) {
	            		System.out.println("\n Resposta : A farmácia não está interessada em negociar..."); }}}
		

		public void retirarStock (String nome, int qt ) {
			for (int j = 0; j < MedList.size(); j++)	{
				String med= MedList.get(j).getnome();
				if (med.equals(nome)) {
					int qtdAtual= MedList.get(j).getquantidade();
					int qtdAtual2 =qtdAtual-qt;
					MedList.get(j).setquantidade(qtdAtual2);
					System.out.println("*************Já atualizei o stock*******************");
					System.out.println(fornecedor.getLocalName()+"\tNome: "+MedList.get(j).getnome()+ "\tPreco:  "+MedList.get(j).getpreco()+"\tQtd: ( "+ qtdAtual+ ") -> "+ MedList.get(j).getquantidade());	
				} 
        	
        }
		}
		
		private class Questionar extends TickerBehaviour
		{
			public Questionar(Agent a, long period) {
			super(a, period);
		}

			@Override	
			protected void onTick() {
			System.out.print("Vou perguntar a farmácia se necessita de medicamentos...");
			Random random5 = new Random();
			int pM = random5.nextInt(MedList.size());
			String nomM =MedList.get(pM).getnome();
			Random random = new Random();
			int div = random.nextInt((maxAgent - minAgent) + 1) + minAgent;
			int qt =MedList.get(pM).getquantidade();
			int tempo = MedList.get(pM).gettempo();
			int peco = MedList.get(pM).getpreco();
			int ped=0;
			if (qt>3) {
				ped = qt/div;
			}else {
				ped = qt;
			}
			String mess=nomM+";"+ped+";"+tempo+";"+peco;
			sendmsg("FarmaciaC", mess, "questionaForn"); 
          }
	}
			
		
	
		private void block(int i) {
			// TODO Auto-generated method stub 
			}
		
		private int verificarStock(String nome, int quantidade) {
			
			for (int j=0; j < MedList.size(); j++)	{
				String h= MedList.get(j).getnome();
				int quant=MedList.get(j).getquantidade();
				int preco = MedList.get(j).getpreco();
				if ((h.equals(nome)) && (quantidade <= quant)) {
					System.out.println("Preço "+preco);
    				melhor = 1;
				}
			}
			return melhor;
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
		
	       
		 public ArrayList<MedicamentosForn> criarMedic (AID agents) {
			 	MedList =  new ArrayList<MedicamentosForn>();
		        String agentnome= agents.getLocalName();
		        Random random = new Random();
		        int numberOfItemsToGenerate = (random.nextInt((maxAgent - minAgent) + 1) + minAgent);
		        for (int i = 0; i < 11 ; i++) {
		        		 int w = random.nextInt(10);
		        		 int preco = random.nextInt((maxAgent - minAgent) + 1) + minAgent;
		        		 int quantidade = random.nextInt(2000);
		        		 int temEntreg = random.nextInt(20); //Em dias
				     MedicamentosForn Medicamento= new MedicamentosForn("M_"+i, preco, quantidade,temEntreg);     
				     MedList.add(Medicamento);      
				    }
		        System.out.println("***Lista de Medicamentos para Fornecedor -> " + fornecedor.getLocalName() +"****");
		        for (int h=0; h<MedList.size();h++) {
		        	System.out.println("\tMedicamento: "+MedList.get(h).getnome()+ 
		        			"\tPreco:  "+MedList.get(h).getpreco()+"\tQtd:  "+MedList.get(h).getquantidade()+
		        			"\tTempo de entrega: "+MedList.get(h).gettempo());
		        }
		        System.out.println("********************************");
		        return MedList;
			 }
		 
		 public void RandomPreco () {
				for (int j = 0; j < MedList.size(); j++)	{
					Random random = new Random();
					int pM = random.nextInt(10);
					int precoOri = MedList.get(j).getpreco();
					MedList.get(j).setpreco(pM+precoOri);
				}
			
	        	
	        }
			
			public void RandomTempo () {
				for (int j = 0; j < MedList.size(); j++)	{
					Random random = new Random();
					int pM = random.nextInt(3);
					int tempoOri = MedList.get(j).gettempo();
					MedList.get(j).settempo(pM+tempoOri);
				}
			
	        	
	        }


	}


	

