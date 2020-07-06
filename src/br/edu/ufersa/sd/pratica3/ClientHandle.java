package br.edu.ufersa.sd.pratica3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

	//Classe ClientHandler 
	class ClientHandler extends Thread  { 
	    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
	    DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
	    final DataInputStream dInput; 
	    final DataOutputStream dOutput; 
	    final Socket socketCliente; 
	      
	  
	    // Constructor 
	    public ClientHandler(Socket socket, DataInputStream dInput, DataOutputStream dOuput)  
	    { 
	        this.socketCliente = socket; 
	        this.dInput = dInput; 
	        this.dOutput = dOuput; 
	    } 
	  
	    @Override
	    public void run()  
	    { 
	        String received; //string para receber dados
	        String toreturn;  // string para retorno teste
	        while (true)  
	        { 
	            try { 
	  
	                // Use uma das opções abaixo
	            	dOutput.writeUTF("Deseja saber | Date | Time]..\n"+ 
	                            "Sair fecha a conexão."); 
	                  
	                // recebendo resposta do cliente
	                received = dInput.readUTF(); 
	                  
	                if(received.equals("Sair")) 
	                {  
	                    System.out.println("Cliente " + this.socketCliente + " enviado Sair..."); 
	                    System.out.println("Fechar a conexão."); 
	                    this.socketCliente.close(); 
	                    System.out.println("Conexão fechada"); 
	                    break; 
	                } 
	                  
	                // Criando um exemplo de data 
	                Date date = new Date(); 
	                  
	                // escrevendo no output stream com base na resposta do cliente
	                switch (received) { 
	                  
	                    case "Date" : 
	                        toreturn = fordate.format(date); 
	                        dOutput.writeUTF(toreturn); 
	                        break; 
	                          
	                    case "Time" : 
	                        toreturn = fortime.format(date); 
	                        dOutput.writeUTF(toreturn); 
	                        break; 
	                          
	                    default: 
	                        dOutput.writeUTF("Entrada inválida"); 
	                        break; 
	                } 
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	            } 
	        } 
	          
	        try
	        { 
	            // fechando recursos streams
	            this.dInput.close(); 
	            this.dOutput.close(); 
	              
	        }catch(IOException e){ 
	            e.printStackTrace(); 
	        } 
	   } 
} 
