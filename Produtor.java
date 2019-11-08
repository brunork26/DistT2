class Produtor extends Thread{
    private int id;
    private int host;
    private int port;
 
    public Produtor(int id, int host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }
}