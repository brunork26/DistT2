import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
    public static void main(String[] args){
        

        //switch(args[4]){  Linha completa de comando com 5 argumentos
        switch(args[0]){
            // Caso Produtor
            case "p":{
            
                break;
            }
            // Caso Consumidor
            case "c":{
                
                break;
            }
            // Processo que nunca morre
            case "a":{
                //Primeira linha do arquivo tem o IP do nodo que nunca morre
                try{
                    BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt"));
                    arquivo.append("NodoBKP - 192.168.1.1");
                    System.out.println("Registro do Nodo que não morre Completo! \n");
                    arquivo.close();
                    abreThreadSocket();
                }catch(Exception ex){
                    System.out.println("Problema na escrita do arquivo/n");
                }

                break;
            }

        }

    }
    // Espera a conexão de algum Nodo Subsequente para gravar no arq.txt 
    // ou operações de recuperação de informações pós eleição
    public static void abreThreadSocket()   {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int porta = 9876;
	        	int numConn = 1;
                
                byte[] recebeDados = new byte[1024];
		        byte[] enviaDados = new byte[1024];
                
                try {     
                    DatagramSocket serverSocket = new DatagramSocket(porta);
               
                    while(true){
                        
                        try {
                            // Pacote UDP de recebimento
                            DatagramPacket pacoteUDP = new DatagramPacket(recebeDados,recebeDados.length);
                            System.out.println("Esperando Conexão dos Nodos... \n");
                        
                            serverSocket.receive(pacoteUDP);

                            System.out.println("Novo Nodo conectado...\n");
                            
                        } catch (Exception e) {
                            System.out.println("\nErro no recebimento do pacote UDP\n");
                        }
                        
                    }
                }catch (Exception e) {
                    System.out.println("Erro na abertura do server Socket");
                }
             }
       }).start();
    }


    // Abre conexão socket e atualiza o arquivo no Nodo que nunca morre
    public void atualizaArquivo(){
        try{

        BufferedReader buffRead = new BufferedReader(new FileReader("./arq.txt"));
        String linha = "";

        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}