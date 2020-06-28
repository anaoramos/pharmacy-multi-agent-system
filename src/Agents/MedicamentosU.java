package Agents;

public class MedicamentosU {
	private  String nome;
    private  int quantidade;
   
    public MedicamentosU(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public   String getnome() {
        return nome;
    }

    public int getquantidade() {
        return quantidade;
    }
    
    public void setquantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String toString() {
        return nome;
    }
}


