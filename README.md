# pdv
Sistema de ERP web desenvolvido em Java com Spring Framework 

# Recursos
- Cadastro produtos/clientes/fornecedor
- Controle de estoque
- Gerenciar comandas
- Realizar venda
- Controle de fluxo de caixa
- Controle de pagar e receber
- Venda com cartões
- Gerenciar permissões de usuários por grupos
- Cadastrar novas formas de pagamentos
- Relatórios

# Instalação
Para instalar o sistema, você deve criar o banco de dado "pdv" no mysql e configurar o arquivo application.properties
com os dados do seu usuário root do mysql e rodar o projeto pelo Eclipse ou gerar o jar do mesmo e execultar.

# Logando no sistema
Para logar no sistema, use o usuário "gerente" e a senha "123".


## Configuração do Banco de Dados

### Passo 1: Criar o Banco de Dados

1. Abra o MySQL e execute o seguinte comando para criar o banco de dados:

    ```sql
    CREATE DATABASE pdv;
    ```

### Passo 2: Criar o Usuário

1. Crie um usuário chamado `gerente` com a senha `123` e conceda privilégios ao banco de dados `pdv`:

    ```sql
    CREATE USER 'gerente'@'localhost' IDENTIFIED BY '123';
    GRANT ALL PRIVILEGES ON pdv.* TO 'gerente'@'localhost';
    FLUSH PRIVILEGES;
    ```

## Instalação da API

### Passo 1: Compilar o Projeto

1. Navegue até a pasta do projeto e execute o comando para compilar e instalar a API:

    ```bash
    mvn clean install
    ```

### Passo 2: Implantar o Arquivo .war no Tomcat

1. Após a compilação, localize o arquivo `.war` gerado na pasta `target`.
2. Copie o arquivo `.war` para a pasta `webapps` do Tomcat:

    ```bash
    cp target/seu_arquivo.war $TOMCAT_HOME/webapps/
    ```

## Executar a API

### Passo 1: Iniciar o Tomcat

1. Navegue até a pasta `bin` do Tomcat e execute o script de inicialização:

    ```bash
    $TOMCAT_HOME/bin/startup.sh
    ```

   **Nota:** No Windows, use `startup.bat`.

2. Verifique se o Tomcat está rodando acessando `http://localhost:8080` no seu navegador.
3. Para acessar a API, deve-se acessar `http://localhost:8080/seu_arquivo

**Nota:** Substitua `seu_arquivo` pelo nome do arquivo `.war` gerado.

## Conclusão

Após seguir os passos acima, a API deve estar configurada e rodando no seu servidor Tomcat, pronta para ser utilizada. Se houver algum problema, verifique os logs do Tomcat na pasta `$TOMCAT_HOME/logs`.

**Lembrete:** Certifique-se de que todas as dependências necessárias estão instaladas e configuradas corretamente no seu ambiente de desenvolvimento.


# Tecnologias utilizadas
- Spring Framework 5
- Thymeleaf 3
- MySQL
- Hibernate
- FlyWay

