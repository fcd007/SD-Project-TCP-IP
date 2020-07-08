# SD-Project-TCP-IP
Uma aplicação cliente/servidor, com TCP ou UDP, que realiza o armazenamento e transferência de arquivos. Considere o seguinte cenário:

## Clientes podem acessar um arquivo a partir de uma lista de disponibilidade, enviada peloservidor.
* Podendo realizar operações de leitura e escrita no arquivo baixado.
* No cliente, deve existir uma opção para encerrar a conexão.
* Servidores realizar os serviços de armazenamento e transferência de arquivos de texto.

* Arquivos são transferidos de acordo com a escolha dos clientes.
* Crie cinco arquivo de texto, contendo assuntos diversos, para essa aplicação.
* A lista de arquivos oferecidos aos clientes pode mudar se o programador do servidor
adicionar mais arquivos passíveis de transferência.
* Pesquise sobre manipulação de arquivos e envio via sockets.

## Para clonar projeto via terminal Linux
* Abra o terminal e navegue até um local que tenha permissão de criação de diretório "Documents"
* Use comando no terminal `git clone https://github.com/fcd007/SD-Project-TCP-IP.git`
* Abra o terminal do Linux e vá até a pasta do diretório do projeto
* Use para entrar no diretório do projeto `cd SD-Project-TCP-IP/src/sd/Project03/

## Executar o projeto clonado via terminal
* Por via das dúvidas compile as classes do java uma de cada vez;
* No terminal executa as etapas seguites:
* `javac FileCliente.java`
* `javac ThreadedFileServer.java`
* `javac FileServer.java`
* Agora pode executar o servidor inicialmente - caso contrário não irá rodar o cliente;
* `java ThreadedFileServer`
* `java FileCliente`
* Caso deseje executar apenas uma thread de uma instância de um servidor que não atende vários cliente
* Execute o FileServer e depois o cliente para ver a solução.

## Dicas ou sugestões serão bem aceitas.
