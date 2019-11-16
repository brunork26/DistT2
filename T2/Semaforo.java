public class Semaforo {
    private int count = 0;
    
    public Semaforo(int initVal) {
        count = initVal;
    }
    
    public synchronized void P() {
        if (count <= 0)
            try { /* esperar até que count > 0 */
                wait();
        } catch (InterruptedException e) { }
        
        count--;
    }
    
    public synchronized void V() {
        count++;
        notify(); /* acordar quem estiver em espera */
    }
}