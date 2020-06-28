package Agents;


public class MedicamentosForn {
  private  String nome;
    private int preco;
    private  int quantidade;
    private int temEntreg;
   
    public MedicamentosForn(String nome, int preco, int quantidade, int temEntreg) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.temEntreg=temEntreg;
    }

    public   String getnome() {
        return nome;
    }

    public  int getpreco() {
        return preco;
    }
    
    public int getquantidade() {
        return quantidade;
    }
    
    public int gettempo() {
        return temEntreg;
    }
    
    public void setquantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public void settempo(int temEntreg) {
        this.temEntreg = temEntreg;
    }
    
    public void setpreco(int preco) {
        this.preco = preco;
    }
    
    public void setnome(String nome) {
        this.nome = nome;
    }
    public String toString() {
        return nome;
    }
}

