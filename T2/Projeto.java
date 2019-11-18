import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;


/*
    1 - Cria o nodo que nunca morre e registra na primeira linha do arquivo
    2 - Abre uma thread com socket que fica esperando os nodos para registrar seus dados no arq.txt
    3 - Nodos se registram e o primeiro nodo que se registra vira o coordenador.
    4 - Nodos subsequentes são Produtores ou Consumidores
    5 - 1 consumidor consome por vez
    6 - 1 Produtor produz por ver
    7 - Semáforos: 
        - Produtores só produzem com buffer vazio
        - 1 produtor por vez
        - Consumidores tem prioridade na chegada de um produtor se o buffer possuir dados

*/
public class Projeto extends Thread  {
    //Status se já tem ou nao coordenador na primeira passagem
    public static String status = "sem coordenador";
    // Consumidor ou produtor
    public static String tipo = "nenhum";

    // Contador que simula a quantidade de dados que tem no Buffer 
    // Contador controlado por um semáforo
    public static int buffer = 10;
    public static String ipNodoQueNaoMorre = "10.32.160.80";
    public static int portaNodoQueNaoMorre = 9876;

    public static String ipCoordenador = "";
    public static int portaCoordenador;

    public static Semaforo sBuffer,sConsumidor,sProdutor;

    public ArrayList<Integer> arConsEspera = new ArrayList<Integer>();
    public ArrayList<Integer> arProdEspera = new ArrayList<Integer>();

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
            byte[] recebeDados = new byte[1024];

            id = args[1];
            host = args[2];
            portaNodo = args[3];
            tipo = args[4];

            // registro TXT nodo que nunca morre
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

                String[] infosConfirmacao = confirmacaoCoord.split("/");                                                          

                System.out.println(infosConfirmacao[0]);

                // Se coordenador, entra no loop que gerencia os Nodos subsequentes e Buffer (Liberando acessos)
                // Se não só 'consome/produz' dados
                if(infosConfirmacao[0].equals("S")){
                    System.out.println("Nodo Coordenador É vc - ID:" + id + "\n");
                    // Thread de atualização do arquivo.txt
                    arqCoordThread();


                    // Coordenação dos eventos 
                    while(true){
                        // Espera recebimento de pacote - Caso seja consumidor ou Produtor
                        serverSocket.receive(pacoteUDP);



                    }
                
                
                }else{
                    // Recebe o IP e a porta do Coordenador
                    ipCoordenador = infosConfirmacao[1];
                    portaCoordenador = Integer.parseInt(infosConfirmacao[2]);

                    System.out.println(ipCoordenador + " - " + portaCoordenador + "\n");
                    
                    // loop de Consumo ou produção
                    
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
                byte[] envioDados = new byte[1024];
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
                                flagCoord = "S/";
                                ipCoordenador = infos[1];
                                portaCoordenador = Integer.parseInt(infos[2]);
                                status = "com coordenador";
                                envioDados = flagCoord.getBytes();
                                
                            }else{
                                // Primeiro envia para o coordenador as informações do novo Nodo que se conectou
                                String infoAtualiz = infos[0] + "/"+ infos[1] + "/" +infos[2];
                                envioDados = infoAtualiz.getBytes();
                                pacoteUDP = new DatagramPacket(envioDados,
                                            envioDados.length, InetAddress.getByName(ipCoordenador) , portaCoordenador);
                                clientSocket.send(pacoteUDP);
                                
                                // Depois, envia para o nodo novo o IP do coordenador 
                                flagCoord  = ipCoordenador+"/"+portaCoordenador;
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

    public static void arqCoordThread(){
        //Envia e recebe dados via socket
        byte[] envioDados = new byte[1024];
        byte[] recebeDados = new byte[1024];
        
        System.out.println("\nThread de Atualização do arquivo do coordenador aberta...\n");
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    try {
                        DatagramSocket serverSocket = new DatagramSocket(portaCoordenador);
                        DatagramPacket pacoteUDP = new DatagramPacket(recebeDados,recebeDados.length);
                        BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt",true));
                        // Espera recebimento de pacote
                        serverSocket.receive(pacoteUDP);
                        
                        // Recebe ID - IP - PORTA do novo NODO conectado 
                        String dadosPacote = new String(pacoteUDP.getData(),
                                            pacoteUDP.getOffset(), pacoteUDP.getLength(),"UTF-8");
                        
                        arquivo.append(dadosPacote);
                        arquivo.close();

                        System.out.println("\nNovo Nodo Conectado...\n");
                        
                    } catch (Exception e) {
                        
                    }
                }
                

            
            
            }
            
    
                   
        }).start();
    }

}