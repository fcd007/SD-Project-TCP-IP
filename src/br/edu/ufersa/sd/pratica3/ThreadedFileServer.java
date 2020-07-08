package br.edu.ufersa.sd.pratica3;

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
    
    //criando um pool de threads fixo
    private static final int THREAD_POOL_SIZE = 30;
    
    /*O comprimento do ArrayBlockingQueue de conexões.
     * Isso não deve ser muito grande, pois as conexões no
     * fila estão esperando por serviço e espero que não
     * gaste muito tempo na fila.
     */
    private static final int CONNECTION_QUEUE_SIZE = 50;
    
    /*A fila usada para enviar conexões do
     * programa principal para os threads de manipulação de conexão.
     * Uma conexão é representada por um soquete conectado.
     */
    private static ArrayBlockingQueue<Socket> connectionQueue;
    
    
    public static void main(String[] args) {

    	String directoryName;  // Directory name entered by the user.
        File directory;        // File object referring to the directory.
        String[] files;        // Array of file names in the directory.
        Scanner scanner;       // For reading a line of input from the user.

        scanner = new Scanner(System.in);  // scanner reads from standard input.

        System.out.print("Nome do diretório: ");
        directoryName = scanner.nextLine().trim();
        
        //Escuta solicitações de conexão.
        ServerSocket listener;
        // Um ​​soquete para comunicação com um cliente.
        Socket connection;
       

        /* Obtenha o nome do diretório faça em um objeto 
         * de arquivo. Verifique se o arquivo existe e
         * é de fato um diretório. */

        directory = new File(directoryName);
        if ( ! directory.exists() ) {
            System.out.println("o diretório não existe.");
            return;
        }
        if (! directory.isDirectory() ) {
            System.out.println("O arquivo não é um diretório.");
            return;
        }
        
        /* criando a fila de conexões, criando as threads*/
        connectionQueue = new ArrayBlockingQueue<Socket>(CONNECTION_QUEUE_SIZE);
        
        //criando o pool de threads
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ConnectionHandler worker = new ConnectionHandler(directory);
            worker.start();
        }

        /* Listar as conexões e adicionar a lista*/
        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Servidor ouvindo na porta: " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                connectionQueue.add(connection);
            }
        }
        catch (Exception e) {
            System.out.println("Servidor encerrado.");
            System.out.println("Error:  " + e);
            return;
        }

    }


    /*A classe que define os segmentos de manipulação de conexão*/
    private static class ConnectionHandler extends Thread {
    	// O diretório que contém os arquivos que estão no servidor.
        File directory;  
        ConnectionHandler(File directory) {
            this.directory = directory;
            setDaemon(true);
        }
        public void run() {
            while (true) {
                try {
                    Socket connection = connectionQueue.take();
                    handleConnection(directory,connection);
                }
                catch (Exception e) {
                }
            }
        }
    }
    
    
    private static void handleConnection(File directory, Socket connection) {
        Scanner incoming;       // para ler dados do cliente
        PrintWriter outgoing;   //para transmitir os dados do cliente
        String command = "Comando não lido";
        try {
            incoming = new Scanner( connection.getInputStream() );
            outgoing = new PrintWriter( connection.getOutputStream() );
            command = incoming.nextLine();
            if (command.equals("INDEX")) {
                sendFileClient(directory, outgoing);
            }
            else if (command.startsWith("get")){
                String fileName = command.substring(3).trim();
                sendFile(fileName, directory, outgoing);
            }
            else {
                outgoing.println("Comando inválido");
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

    /*
     * Isso é chamado pelo método run() em resposta a um comando envio
     * do cliente. Envie a lista de arquivos no diretório do servidor.
     */
    private static void sendFileClient(File directory, PrintWriter outgoing) throws Exception {
        String[] fileList = directory.list();
        for (int i = 0; i < fileList.length; i++)
            outgoing.println(fileList[i]);
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Erro enquanto enviada os dados.");
    }

    /*chamando pelo run() resposta  ao cliente
     */
    private static void sendFile(String fileName, File directory, PrintWriter outgoing) throws Exception {
        File file = new File(directory,fileName);
        if ( (! file.exists()) || file.isDirectory() ) {
            // erro se enviar um diretório que não existe
            outgoing.println("Error");
        }
        else {
            outgoing.println("ok");
            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
            while (true) {
                // Read and send lines from the file until
                // an end-of-file is encountered.
                String line = fileIn.readLine();
                if (line == null)
                    break;
                outgoing.println(line);
            }
        }
        outgoing.flush(); 
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error durante transferência.");
    }


}
