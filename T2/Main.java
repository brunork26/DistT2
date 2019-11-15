import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    1 - Cria o nodo que nunca morre e registra na primeira linha do arquivo
    2 - Abre uma thread com socket que fica esperando os nodos para registrar seus dados no arq.txt
    3 - 













*/
public class Main extends Thread  {
    String status = "";
    // Contador que simula a quantidade de dados que tem no Buffer 
    // Contador controlado por um semáforo
    int buffer = 10;
    public static String ipNodoQueNaoMorre = "localhost";
    public static int portaNodoQueNaoMorre = 9876;
    public static void main(String[] args){

        // Processo que nunca morre
        if(args.length == 1){
            //Primeira linha do arquivo tem o IP do nodo que nunca morre
            try{
                BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt"));
                arquivo.append("NodoBKP - " + args[0] + "\n");
                System.out.println("Registro do Nodo que não morre Completo! \n");
                arquivo.close();
                // Inicia Thread/Servidor
                abreThreadSocket();
            }catch(Exception ex){
                System.out.println("Problema na escrita do arquivo/n");
            }

        // Produtor ou Consumidor
        }else{
    
            String host = "", id = "", portaNodo = "";
            String registro = "";
            
            byte[] envioDados = new byte[1024];

            id = args[1];
            host = args[2];
            portaNodo = args[3];

            registro = id + "/" + host + "/" + portaNodo;
            envioDados = registro.getBytes();
  
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                DatagramPacket pacoteUDP = new DatagramPacket(envioDados,
                    envioDados.length, InetAddress.getByName(ipNodoQueNaoMorre) , portaNodoQueNaoMorre);

                clientSocket.send(pacoteUDP);
                clientSocket.close();
            } catch (Exception e) {
                
            }
        }
    

    }
    // Espera a conexão de algum Nodo Subsequente para gravar no arq.txt 
    // ou operações de recuperação de informações pós eleição
    public static void abreThreadSocket()   {
        new Thread(new Runnable() {
            @Override
            public void run() {

                byte[] recebeDados = new byte[1024];
                
                try {     
                    DatagramSocket serverSocket = new DatagramSocket(portaNodoQueNaoMorre);
                                  
                    while(true){
                        
                        try {
                            
                            BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt",true));
                            // Pacote UDP de recebimento
                            DatagramPacket pacoteUDP = new DatagramPacket(recebeDados,recebeDados.length);
                            System.out.println("Esperando Conexão dos Nodos... \n");
                            
                            // Espera recebimento de pacote
                            serverSocket.receive(pacoteUDP);
                            
                            System.out.println("Novo Nodo conectado...\n");

                            String dadosPacote = new String(pacoteUDP.getData());
                            System.out.println("ID/IP/PORTA - " + dadosPacote + "\n");

                            arquivo.append(dadosPacote);
                            arquivo.append("\n") ;
                            arquivo.close();

                        } catch (Exception e) {
                            System.out.println("\nErro no recebimento do pacote UDP ou na escrita do arquivo\n");
                            System.out.println(e.getMessage() + "\n");
 
                        } 
                        
                    }
                }catch (Exception e) {
                    System.out.println("Erro na abertura do server Socket");
                }
             }
       }).start();
    }


    // // Abre conexão socket e atualiza o arquivo no Nodo que nunca morre
    // public void atualizaArquivo(){
    //     try{

    //     BufferedReader buffRead = new BufferedReader(new FileReader("./arq.txt"));
    //     String linha = "";

    //     }catch(Exception ex){
    //         System.out.println(ex.getMessage());
    //     }

    // }
}