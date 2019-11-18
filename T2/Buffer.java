public class Buffer {
 
    private int conteudo;
    private boolean disponivel;
 
    public synchronized void set(int idProdutor, int valor) {
        while (disponivel == true) {
            try {
                System.out.println("Produtor #" + idProdutor + " esperando...");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(conteudo == 0 ) {
            conteudo += valor;
        }
        
        System.out.println("Produtor #" + idProdutor + " tem " + conteudo);
        disponivel = true;
        notifyAll();
    }
 
    public synchronized int get(int idConsumidor) {
        while (disponivel == false) {
            try {
                System.out.println("Consumidor #" + idConsumidor
                        + " esperado...");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumidor #" + idConsumidor + " conteudo: "
                + conteudo);
        disponivel = false;
        if(conteudo > 0 ) {
            conteudo -= 1;
        }
        notifyAll();
        return conteudo;
    }

    public int getConteudo() {
        return conteudo;
    }
}