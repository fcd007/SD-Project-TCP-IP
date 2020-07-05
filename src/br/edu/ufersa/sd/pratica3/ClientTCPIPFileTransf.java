package br.edu.ufersa.sd.pratica3;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCPIPFileTransf {

	public static void main(String[] args) throws Exception {
		
		if(args.length != 1) {
			System.out.println("Passa o IP do servidor como único argumento de linha de comando");
			return;
		}
		
		try (Socket socket = new Socket(args[0], 3400)) {
			//entrada de dados
			Scanner scanner = new Scanner(System.in);
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			while (scanner.hasNextLine()) {
				out.println(scanner.nextLine());
				System.out.println(in.nextLine());
			}
			scanner.close();
		}
	}

}
