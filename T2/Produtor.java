import java.io.*;
import java.net.*;

public class Produtor {
    private int idProdutor;
    private int producaoTotal;
 
    public Produtor(int id, int producaoTotal) {
        idProdutor = id;
        this.producaoTotal = producaoTotal;
    }
    
    public void produzir(String s, int p) {
        // Pede acesso a thread execução
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            while(true) {
            String servidor = s;
            int porta = p;
    
            InetAddress IPAddress = InetAddress.getByName(servidor);
    
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
    
            System.out.println("Pedindo para produzir");
            String sentence = "p " + this.idProdutor + " " + this.producaoTotal;
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
            }
        } catch(Exception e) {

        }
    }

    // public void run() {
    //     for (int i = 0; i < producaoTotal; i++) {
    //         pilha.set(idProdutor, i);
    //     }
    //     System.out.println("Produtor #" + idProdutor + " concluido!");
    // }
}