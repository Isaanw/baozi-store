package br.com.baozistore.controller;

import br.com.baozistore.model.Pedido;
import br.com.baozistore.repository.PedidoRepository;
import br.com.baozistore.model.Cliente;
import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ClienteRepository;
import br.com.baozistore.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository repository, ClienteRepository clientes, ProdutoRepository produtos) {
        this.repository = repository;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    public record Cadastro(
            @NotNull(message = "Informe o ID do cliente.") @Positive(message = "O ID do cliente deve ser positivo.") Long clienteId,
            @NotNull(message = "Informe o ID do produto.") @Positive(message = "O ID do produto deve ser positivo.") Long produtoId,
            @NotNull(message = "Informe a quantidade.") @Positive(message = "A quantidade deve ser maior que zero.") Integer quantidade) {
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criar(@Valid @RequestBody Cadastro dados) {
        Cliente cliente = clientes.findById(dados.clienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
        Produto produto = produtos.findById(dados.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
        Pedido salvo = repository.saveAndFlush(new Pedido(cliente, produto, dados.quantidade()));
        return ResponseEntity.created(URI.create("/pedidos/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public List<Pedido> listar() {
        return repository.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public Pedido consultar(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }
}
