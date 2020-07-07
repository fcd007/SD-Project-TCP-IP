package sd.Project03;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
   
public class FileClient {

   static final int LISTENING_PORT = 3400;


   public static void main(String[] args) {
	  //Nome ou endereço IP do servidor.
      String computer; 
      //Socket para conexões
      Socket connection;
      //comando para servidor
      PrintWriter outgoing; 
      //Leitura de dados da conexão
      BufferedReader incoming;  
      //solicitação de recursos
      String requestResources;
      

      /*Entrada de argumentos dia linha de comando */
      
      if (args.length == 0 || args.length > 3) {
         System.out.println("Use os comandos a seguir:  java FileClient <server>");
         System.out.println("    ou  java FileClient <server> <file>");
         System.out.println(
               "    ou  java FileClient <server> <file> <local-file>");
         return;
      }
      
      /* envio de mensagem para servidor */
      
      computer = args[0];
      
      if (args.length == 1)
         requestResources = "INDEX";
      else
         requestResources = "GET " + args[1];
      
      /* Realizando as entradas e saídas*/
      
      try {
         connection = new Socket( computer, LISTENING_PORT );
         incoming = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
         outgoing = new PrintWriter( connection.getOutputStream() );
         outgoing.println(requestResources);
         outgoing.flush();
      }
      catch (Exception e) {
         System.out.println(
              "Erro ao conectar ao servidor \"" + args[0] + "\".");
         System.out.println("Erro:  " + e);
         return;
      }
      
      /* ler e processar dados no servidor */
      
      try {
         if (args.length == 1) {
               // Ler as linhas para o comando index
            System.out.println("Arquivos dentro do servidor:");
            while (true) {
               String line = incoming.readLine();
               if (line == null)
                   break;
               System.out.println("   " + line);
            }
         }
         else {
               // leitura do arquivo do servidor
               //  resposta com o comando desejado
            String message = incoming.readLine();
            if (! message.equalsIgnoreCase("OK")) {
               System.out.println("O arquivo não existe.");
               System.out.println("Mensagem servidor: " + message);
               return;
            }
            PrintWriter fileOut;  // escrever arquivo em um file
            if (args.length == 3) {
                  // file name para argumento
                fileOut = new PrintWriter( new FileWriter(args[2]) );
            }
            else {
                  // segundo argmento nome file
                File file = new File(args[1]);
                if (file.exists()) {
                   System.out.println("O arquivo já existe.");
                   return;
                }
                fileOut = new PrintWriter( new FileWriter(args[1]) );
            }
            while (true) {
                   // Copy lines from incoming to the file until
                   // the end of the incoming stream is encountered.
                String line = incoming.readLine();
                if (line == null)
                    break;
                fileOut.println(line);
            }
            if (fileOut.checkError()) {
               System.out.println("Um erro ocorrreu.");
            }
         }
      }
      catch (Exception e) {
         System.out.println(
                 "Erro ao ler dados do arquivo servidor.");
         System.out.println("Error: " + e);
      }
      finally {
         try {
            connection.close();
         }
         catch (IOException e) {
         }
      }
   } 
}
