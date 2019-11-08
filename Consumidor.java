class Consumidor extends Thread {
    private int id;
    private int host;
    private int port;

    public Consumidor(int id, int host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }
}