package br.edu.ufersa.sd.pratica3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket; 

//Java implementation of  Server side 
//It contains two classes : Server and ClientHandler 
//Save file as Server.java 
	  
	// Server class Server sockets 
	public class ServerTCPIPFileTransf { 
		
	    public static void main(String[] args) throws IOException  { 
	    	
	        // sServidor iniciado na porta 3400
	        try(ServerSocket socketServer = new ServerSocket(3400)) {
	        	System.out.println("Iniciando servidor porta " + socketServer.getLocalPort());
	        	
		        // criando um loop infinito para requisições de clientes
		        while (true)  { 
		        	
		        	Socket socket = null; 
		              
		            try 
		            { 
		                // objeto socket para aceitar requisições dos clientes
		                socket = socketServer.accept(); 
		                  
		                System.out.println("Novo cliente conectado: " + socket); 
		                  
		                // obtendo entrada e saída de streams
		                DataInputStream dInput = new DataInputStream(socket.getInputStream()); 
		                DataOutputStream dOutput = new DataOutputStream(socket.getOutputStream()); 
		                  
		                System.out.println("Cliente adicionado a uma nova thread..."); 
		  
		                // Criando um objeto thread
		                Thread thread = new ClientHandler(socket, dInput, dOutput); 
		  
		                // invocar o método para iniciar thread
		                thread.start(); 
		                  
		            } 
		            catch (Exception e){ 
		                socket.close(); 
		                e.printStackTrace(); 
		            } 
		        } 
		    }
	    }
}
