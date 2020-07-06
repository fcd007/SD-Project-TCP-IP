package br.edu.ufersa.sd.pratica3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCPIPFileTransf {
	
	 public static void main(String[] args) throws Exception{
		 
		 //criando o servidor porta 3400
		 try(ServerSocket listenerServer = new ServerSocket(3400)) {
			 System.out.println("Servidor em execução... " + listenerServer.getLocalPort());
			 
			 //definindo o pool de tamanho fixo de threads
			 System.out.println("Criando pool de threads... ");
			 ExecutorService poolThreads = Executors.newFixedThreadPool(30);
			 
			 while (true) {
				poolThreads.execute(new FileTransfer(listenerServer.accept()));
				System.out.println("Nova conexão recebida...");
			}
		 }
	}

	//criando a classe para tratar as requisições dos clientes
	private static class FileTransfer implements Runnable {
		
		private Socket socket;
		
		FileTransfer(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			System.out.println("Conectado: " + socket);
			try {
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				while(in.hasNextLine()) {
					out.println(in.nextLine().toUpperCase()); //apenas executa uma tarefa simples
				}
			}catch (Exception e) {
				System.out.println("Error: " + socket);
			}finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.getMessage();
				}
				System.out.println("Closed: " + socket);
			}
		}
	}

}



