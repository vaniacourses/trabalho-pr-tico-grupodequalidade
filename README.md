# Projeto PDV
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

# Logando no sistema
Para logar no sistema, use o usuário "gerente" e a senha "123".

# Questionário ISO/IEC 25010 - Usado nos slides

## Funcionalidade
1. O software atende aos requisitos funcionais especificados?
2. O software executa todas as funções necessárias sem erros?
3. As funcionalidades do software são completas e suficientes para atender às necessidades do usuário?

## Confiabilidade
4. O software opera de forma consistente sob condições especificadas?
5. O software possui mecanismos adequados para recuperação de falhas?
6. O software mantém seu desempenho em níveis aceitáveis durante o uso contínuo?

## Usabilidade
7. A interface do usuário é intuitiva e fácil de usar?
8. O software fornece ajuda e documentação adequada ao usuário?
9. O software é fácil de aprender para novos usuários?

## Eficiência
10. O tempo de resposta do software está dentro dos limites aceitáveis?
11. O software utiliza os recursos do sistema de forma eficiente?
12. O software é capaz de lidar com grandes volumes de dados sem degradação significativa no desempenho?

## Manutenibilidade
13. O software é fácil de modificar para corrigir defeitos?
14. O software é bem documentado para facilitar a manutenção?
15. O software é modular e permite a substituição de componentes sem afetar o sistema inteiro?

## Portabilidade
16. O software pode ser transferido para diferentes ambientes de hardware e software sem necessidade de grandes modificações?
17. O software é compatível com diferentes sistemas operacionais?
18. O software suporta múltiplos idiomas e configurações regionais?

## Segurança
19. O software protege os dados do usuário contra acesso não autorizado?
20. O software possui mecanismos para garantir a integridade dos dados?
21. O software inclui funcionalidades de auditoria e registro de atividades?

## Compatibilidade
22. O software é compatível com outros sistemas e aplicativos que precisa integrar?
23. O software lida adequadamente com a troca de dados entre diferentes sistemas?
24. O software segue padrões de interoperabilidade estabelecidos?

## Instruções
Para cada pergunta, avalie o software em uma escala de 1 a 5, onde:
- 1 = Muito insatisfatório
- 2 = Insatisfatório
- 3 = Satisfatório
- 4 = Bom
- 5 = Excelente
# Tecnologias utilizadas
- Spring Framework 5
- Thymeleaf 3
- MySQL
- Hibernate
- FlyWay

