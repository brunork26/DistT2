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
public class Projeto extends Thread  {
    //Status se já tem ou nao coordenador na primeira passagem
    public static String status = "sem coordenador";
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
                arquivo.flush();
                arquivo.close();
                // Inicia Thread/Servidor
                abreThreadSocket();
            }catch(Exception ex){
                System.out.println("Problema na escrita do arquivo/n");
            }

        // Produtor ou Consumidor
        }else{
    
            // Primeiro salva as informaçoes no arquivo txt
            String host = "", id = "", portaNodo = "";
            String registro = "";
            
            byte[] envioDados = new byte[1024];

            //Byte de confirmação se é ou nao o coordenador quando inicia o processo
            byte[] recebeDados = new byte[1];

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

                // Após registro, necessário saber se é o primeiro nodo a se registrar
                // Se for, então é coordenador

                DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(args[3]));
                pacoteUDP = new DatagramPacket(recebeDados,recebeDados.length);
                
                System.out.println("Esperando Confirmação se Nodo é Coordenador... \n");
                
                 // Espera recebimento de pacote
                serverSocket.receive(pacoteUDP);

                String confirmacaoCoord = new String(pacoteUDP.getData(),
                                      pacoteUDP.getOffset(), pacoteUDP.getLength(),"UTF-8");

                System.out.println(confirmacaoCoord);

                // Se coordenador, entra no loop que gerencia os Nodos subsequentes e Buffer (Liberando acessos)
                // Se não só 'consome/produz' dados
                if(confirmacaoCoord.equals("S")){
                    System.out.println("Sou o Coordenador MOTHAFOCKA");
                }else{

                    System.out.println("Sou Ninguem");
                    
                }
                serverSocket.close();

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

                //Envia Confirmação de coordenador 
                byte[] envioDados = new byte[1];
                byte[] recebeDados = new byte[1024];
                
                try {     
                    
                    while(true){
                        
                        try {
                            DatagramSocket serverSocket = new DatagramSocket(portaNodoQueNaoMorre);
                            DatagramSocket clientSocket = new DatagramSocket(); 
                            BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt",true));
                            // Pacote UDP de recebimento
                            DatagramPacket pacoteUDP = new DatagramPacket(recebeDados,recebeDados.length);
                            System.out.println("Esperando Conexão dos Nodos... \n");
                            
                            // Espera recebimento de pacote
                            serverSocket.receive(pacoteUDP);
                            
                            System.out.println("Novo Nodo conectado...\n");

                            String dadosPacote = new String(pacoteUDP.getData(),
                                                 pacoteUDP.getOffset(), pacoteUDP.getLength(),"UTF-8");

                            System.out.println("ID/IP/PORTA - " + dadosPacote + "\n");

                            arquivo.append(dadosPacote + "\n");
                            arquivo.flush();
                            arquivo.close();
                            serverSocket.close();

                            
                            // infos:
                            // 0 - id 
                            // 1 - host
                            // 2 - porta
                            String[] infos = dadosPacote.split("/");
                            String flagCoord = "";
                            
                            System.out.println("Enviando Confirmação de Coordenador\n");

                             // Envio da confirmação se é ou nao o coordenador
                            if(status.equals("sem coordenador")){
                                flagCoord = "S";
                                status = "com coordenador";
                                envioDados = flagCoord.getBytes();
                                
                            }else{
                                flagCoord = "N";
                                envioDados = flagCoord.getBytes();
                            }

                            pacoteUDP = new DatagramPacket(envioDados,
                                            envioDados.length, InetAddress.getByName(infos[1]) , Integer.parseInt(infos[2]));
                            
                            clientSocket.send(pacoteUDP);
                            clientSocket.close();

                            System.out.println("Confirmação enviada\n");

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

}