package sd.Project03;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FileServer {

   static final int LISTENING_PORT = 3400;


   public static void main(String[] args) {
	  //ciretório de arquivos
      File directory;       
      //requisições observador
      ServerSocket listener;
      //conexão socket
      Socket connection;  


      /* avaliando  a requisição*/
      if (args.length == 0) {
         System.out.println("Suse a linha de comando:  java FileServer <diretório>");
         return;
      }

      /* Diretório verificação*/

      directory = new File(args[0]);
      if ( ! directory.exists() ) {
         System.out.println("Diretório nao existe.");
         return;
      }
      if (! directory.isDirectory() ) {
         System.out.println("Não é um diretório.");
         return;
      }

      /* listando as conexões de clientes. */

      try {
         listener = new ServerSocket(LISTENING_PORT);
         System.out.println("Servidor na porta: " + LISTENING_PORT);
         while (true) {
            connection = listener.accept();
            handleConnection(directory,connection);
         }
      }
      catch (Exception e) {
         System.out.println("Servidor encerrado..");
         System.out.println("Error:  " + e);
         return;
      }

   } // end main()


   private static void handleConnection(File directory, Socket connection) {
      Scanner incoming;       // lendo dados do cliente
      PrintWriter outgoing;   // enviando dados do cliente
      String command = "não pude ser lido";
      try {
         incoming = new Scanner( connection.getInputStream() );
         outgoing = new PrintWriter( connection.getOutputStream() );
         command = incoming.nextLine();
         if (command.equalsIgnoreCase("index")) {
            sendIndex(directory, outgoing);
         }
         else if (command.toLowerCase().startsWith("get")){
            String fileName = command.substring(3).trim();
            sendFile(fileName, directory, outgoing);
         }
         else {
            outgoing.println("ERROR solicitação errada");
            outgoing.flush();
         }
         System.out.println("OK    " + connection.getInetAddress()
               + " " + command);
      }
      catch (Exception e) {
         System.out.println("ERROR " + connection.getInetAddress()
               + " " + command + " " + e);
      }
      finally {
         try {
            connection.close();
         }
         catch (IOException e) {
         }
      }
   }

   /* Fazendo o envio do arquivo*/
   private static void sendIndex(File directory, PrintWriter outgoing) throws Exception {
      String[] fileList = directory.list();
      for (int i = 0; i < fileList.length; i++)
         outgoing.println(fileList[i]);
      outgoing.flush();
      outgoing.close();
      if (outgoing.checkError())
         throw new Exception("Error o transferir dados");
   }

   /**/
   private static void sendFile(String fileName, File directory, PrintWriter outgoing) throws Exception {
      File file = new File(directory,fileName);
      if ( (! file.exists()) || file.isDirectory() ) {
         // erro eo enviar para o diretório os dados do arquivo.
         outgoing.println("ERROR");
      }
      else {
         outgoing.println("OK");
         BufferedReader fileIn = new BufferedReader( new FileReader(file) );
         while (true) {
            // lendo cada linha e enviando
            String line = fileIn.readLine();
            if (line == null)
               break;
            outgoing.println(line);
         }
      }
      outgoing.flush(); 
      outgoing.close();
      if (outgoing.checkError())
         throw new Exception("Error de envio de dados.");
   }


}
