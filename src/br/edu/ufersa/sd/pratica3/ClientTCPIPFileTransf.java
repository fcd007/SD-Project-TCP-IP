package br.edu.ufersa.sd.pratica3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner; 

	// Classe cliente
	public class ClientTCPIPFileTransf {
		
		public static void main(String[] args) throws IOException { 
			try { 
				Scanner scanner = new Scanner(System.in); 
				
				// Iniciando host localhost
				InetAddress address = InetAddress.getByName("localhost"); 
		
				// estabelecendo conexão via porta 3400
				Socket socket = new Socket(address, 3400); 
		
				// obtendo dados de entrada via streams
				DataInputStream dInput = new DataInputStream(socket.getInputStream()); 
				DataOutputStream dOuput = new DataOutputStream(socket.getOutputStream()); 
		
				// loop dados vindos do cliente e do clienteHandler 
				while (true) 
				{ 
					System.out.println(dInput.readUTF()); 
					String tosend = scanner.nextLine(); 
					dOuput.writeUTF(tosend); 
					
					// o cliente fica no loop enquanto não digitar "Sair"
					if(tosend.equals("Sair")) 
					{ 
						System.out.println("Fechando conexão : " + socket); 
						socket.close(); 
						System.out.println("Conexão fechada"); 
						break; 
					} 
					
					// mostrando data recebida pelo cliente
					String received = dInput.readUTF(); 
					System.out.println(received); 
				} 
				
				// fechando recursos
				scanner.close(); 
				dInput.close(); 
				dOuput.close(); 
			}catch(Exception e){ 
				e.printStackTrace(); 
			} 
		} 
	} 


