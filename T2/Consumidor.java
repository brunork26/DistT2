import java.io.*;
import java.net.*;


public class Consumidor {
    private int idConsumidor;
    private int totalConsumir;
 
    public Consumidor(int id, int totalConsumir) {
        idConsumidor = id;
        this.totalConsumir = totalConsumir;
    }

    public void consumir(String s, int p) {
        // Pede acesso a thread execução
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            String servidor = s;
            int porta = p;
    
            InetAddress IPAddress = InetAddress.getByName(servidor);
    
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
    
            System.out.println("Pedindo para Consumir...");
            String sentence = "c " + this.idConsumidor + " " + this.totalConsumir;
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData,
                    sendData.length, IPAddress, porta);
    
            System.out
                    .println("Enviando pacote UDP para " + servidor + ":" + porta);
            clientSocket.send(sendPacket);
    
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);
    
            clientSocket.receive(receivePacket);
            System.out.println("Pacote UDP recebido...");
    
            String modifiedSentence = new String(receivePacket.getData());
    
            System.out.println("Texto recebido do servidor:" + modifiedSentence);
            clientSocket.close();
            System.out.println("Socket cliente fechado!");
        } catch(Exception e) {

        }
    }
 
    // public void run() {
    //     for (int i = 0; i < totalConsumir; i++) {
    //         pilha.get(idConsumidor);
    //     }
    //     System.out.println("Consumidor #" + idConsumidor + " concluido!");
    // }
}