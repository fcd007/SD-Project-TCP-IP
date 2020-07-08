import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadedFileServer {

    static final int LISTENING_PORT = 3400;
    
//criar um pool de threads
    private static final int THREAD_POOL_SIZE = 30;
    
//definindo uma lista de conexões
    private static final int CONNECTION_QUEUE_SIZE = 50;
//um array para tratar a nossa lista de conexões
    private static ArrayBlockingQueue<Socket> connectionQueue;
    
    
    public static void main(String[] args) {

    	//arquivo de diretório
        File directory;
        //lista de requisições
        ServerSocket listener; 
        //conectar cliente
        Socket connection;   


        /* avaliando  a requisição*/
        if (args.length == 0) {
           System.out.println("Use a linha de comando:  java FileServer <diretório>");
           return;
        }
        
        //verificando a solicitação do argumento
        directory = new File(args[0]);
        if ( ! directory.exists() ) {
            System.out.println("Diretório nao existe.");
            return;
        }
        if (! directory.isDirectory() ) {
            System.out.println("Não é um diretório.");
            return;
        }
        
        //criando os recursos de conexão e array threads
        connectionQueue = new ArrayBlockingQueue<Socket>(CONNECTION_QUEUE_SIZE);
        
        //criando o pool de threads
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ConnectionHandler worker = new ConnectionHandler(directory);
            worker.start();
        }

        /* Listar as conexões e adicionar a lista*/

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Servidor listado na porta " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                connectionQueue.add(connection);
            }
        }
        catch (Exception e) {
            System.out.println("Servidor encerrou do nada!.");
            System.out.println("Error:  " + e);
            return;
        }

    } // end main()


    /**
     * a classe que define a criação das threads
     */
    private static class ConnectionHandler extends Thread {
        File directory;  // diretório que contém os arquivos no servidor
        ConnectionHandler(File directory) {
            this.directory = directory;
            setDaemon(true);
        }
        public void run() {
            while (true) {
                try {
                    Socket connection = connectionQueue.take();
                    handleConnection(directory, connection);
                }
                catch (Exception e) {
                }
            }
        }
    }
    
    
    private static void handleConnection(File directory, Socket connection) {
        Scanner incoming;       // lendo do cliente
        PrintWriter outgoing;   // envio para cliente
        String command = "Comando não lido";
        try {
            incoming = new Scanner( connection.getInputStream() );
            outgoing = new PrintWriter( connection.getOutputStream() );
            command = incoming.nextLine();
            if (command.equals("INDEX")) {
                sendIndex(directory, outgoing);
            }
            else if (command.startsWith("GET")){
                String fileName = command.substring(3).trim();
                sendFile(fileName, directory, outgoing);
            }
            else {
                outgoing.println("Comando não suportado");
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

  
   	//enviar os dados do arquivo
    private static void sendIndex(File directory, PrintWriter outgoing) throws Exception {
        String[] fileList = directory.list();
        for (int i = 0; i < fileList.length; i++)
            outgoing.println(fileList[i]);
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Erro de transferência de dados");
    }

    //enviar arquivo 
    private static void sendFile(String fileName, File directory, PrintWriter outgoing) throws Exception {
        File file = new File(directory,fileName);
        if ( (! file.exists()) || file.isDirectory() ) {
            outgoing.println("error");
        }
        else {
            outgoing.println("ok");
            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
            while (true) {
               //lendo linhas
                String line = fileIn.readLine();
                if (line == null)
                    break;
                outgoing.println(line);
            }
        }
        outgoing.flush(); 
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Erro de transferência de dados.");
    }


}
