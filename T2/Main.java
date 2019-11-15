import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.InetAddress;

public class Main {
    String status = "";
    int buffer = 10;
    public static void main(String[] args) {
        

        //switch(args[4]){  Linha completa de comando 
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
                //Primeira linha do arquivo tem o IP do coordenador
                try{
                    BufferedWriter arquivo = new BufferedWriter(new FileWriter("./arq.txt"));
                    arquivo.append("Coordenador: 192.168.1.1\n");
                    System.out.println("Registro do Nodo que não morre Completo! \n");
                    arquivo.close();
                }catch(Exception ex){
                    System.out.println("Problema na escrita do arquivo/n");
                }
                
                break;
            }

        }

    }
}