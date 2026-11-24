# Baozi Store

API REST da atividade prática de Desenvolvimento Web Back-End. O sistema registra clientes, produtos e pedidos de um único produto por pedido.

## Executar

Use JDK 17 ou superior compatível com Spring Boot 4.0.10. O Maven Wrapper baixa o Maven 3.9.11 e as dependências na primeira execução. Abra o terminal na pasta que contém o pom.xml.

**Windows**

    .\mvnw.cmd clean package
    java -jar target/baozi-store-1.0.0.jar

**Linux ou macOS**

    sh mvnw clean package
    java -jar target/baozi-store-1.0.0.jar

A API atende em http://127.0.0.1:8080 e fica disponível apenas no computador local. Encerre com Ctrl+C.

## Como funciona

- model contém as entidades JPA, com IDs Long gerados pelo banco.
- repository usa Spring Data JPA para salvar, consultar e excluir registros.
- controller recebe e devolve JSON via Spring MVC. Os records Cadastro descrevem os campos de entrada e definem as validações.
- Pedido tem duas associações ManyToOne. O controlador busca cliente e produto pelos IDs recebidos. O JSON de resposta traz os dois objetos associados.
- H2 grava em data/baozi.mv.db, relativo à pasta de execução. O Hibernate cria ou atualiza as tabelas ao iniciar. O usuário local é sa e a senha está vazia por padrão. A configuração fica em src/main/resources/application.properties.
- estoque indica disponibilidade cadastrada. O pedido registra a quantidade solicitada e a API mantém a disponibilidade como cadastrada.
- O banco impede excluir cliente ou produto referenciado. A API responde 409. Exclua o pedido antes das entidades associadas.

## Endpoints

| Operação | Cliente | Produto | Pedido | Sucesso |
|---|---|---|---|---|
| Criar | POST /clientes | POST /produtos | POST /pedidos | 201 |
| Listar | GET /clientes | GET /produtos | GET /pedidos | 200 |
| Consultar | GET /clientes/{id} | GET /produtos/{id} | GET /pedidos/{id} | 200 |
| Excluir | DELETE /clientes/{id} | DELETE /produtos/{id} | DELETE /pedidos/{id} | 204 |

A criação devolve o registro com ID e o cabeçalho Location. O código 400 indica entrada inválida e 404 indica recurso ou referência inexistente.

### Exemplos de criação

**Cliente**

    {"nome":"Isadora da Silva5171883","clienteDesde":"2026-11-23"}

**Produto**

    {"nome":"Baozi de carne suína","preco":12.00,"estoque":true}

**Pedido com IDs ilustrativos**

    {"clienteId":1,"produtoId":1,"quantidade":3}

## Evidências no Postman

1. Com a API em execução, abra o Postman Desktop e use **Import** para selecionar **Baozi-Store.postman_collection.json**.
2. Na coleção Baozi Store, confirme a variável baseUrl. Execute os itens numerados de 01 a 12 com **Send**. A coleção armazena cada ID após o POST correspondente. O Runner executa a sequência inteira e também exclui os registros de demonstração no final.
3. Tire uma captura depois de cada operação. Deixe visíveis método, URL, status e resposta. Nos POST, abra a aba Body com o JSON da requisição. Se precisar mostrar os valores resolvidos na captura, substitua as variáveis da URL ou do corpo pelos IDs retornados neste teste, ou mostre seus valores ao passar o mouse. Amplie a janela para que o JSON fique legível.
4. Nos DELETE, registre o status 204 com o corpo vazio. A coleção exclui pedido, produto e cliente nessa ordem.
5. Insira as 12 capturas na seção Prints do Postman do DOCX e adicione o link real do repositório. Remova a indicação de rascunho e exporte para PDF.

## Publicar o código

Crie um repositório vazio no GitHub ou similar com a visibilidade adequada ao acesso da correção. Revise os dados de identificação presentes na coleção e nos exemplos antes de autorizar acesso público. O endereço abaixo é um marcador e deve ser substituído pelo endereço do repositório criado.

    git init -b main
    git add .
    git add --chmod=+x mvnw
    git status --short
    git commit -m "Implementa API REST Baozi Store"
    git remote add origin ENDERECO_DO_REPOSITORIO
    git push -u origin main
