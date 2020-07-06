<%@ page import="java.io.*, java.net.*, java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cliente Arquivos</title>
</head>
<body>
<% 

		

		if(args.length != 1){
		System.out.println("Passa o IP do servidor como único argumento de linha de comando");
		return;
		
        try(Socket socket = new Socket(args[0], 3400)){
        /*    int character;
            
            InputStream inSocket = socket.getInputStream();
            OutputStream outSocket = socket.getOutputStream();
            String str = "Hello!\n";
            byte buffer[] = str.getBytes();
            outSocket.write(buffer);
            while ((character = inSocket.read()) != -1) {
                out.print((char) character);
            }
            socket.close(); */
            
    		%>
    		<input type="button" value="Fechar Conexão" onclick="socket.close()"/>
    		<input type="button" value="Upar Arquivo" onclick=""/>
    		<% 
    		
            
			//entrada de dados
			Scanner scanner = new Scanner(System.in);
			Scanner in = new Scanner(socket.getInputStream());
            
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    	//	Object obj = new Object(); // generico p substituir o dao
    	 //	ArrayList<Object> listaObjetos = new ArrayList<Object>();// ou obj.getList se tiver implementado
    	 	int i; // incremento
    		while (scanner.hasNextLine()) {
    			out.println(scanner.nextLine());  
    			System.out.println(in.nextLine());  
    		}
    		scanner.close();
        }
        catch(java.net.ConnectException e){
        %>
            You must first start the server application 
            (ServerTCPIPFileTransf.java) at the command prompt.
        <%
        }
        
       
       
}
        %>
</body>
</html>