package br.com.baozistore.controller;

import br.com.baozistore.model.Produto;
import br.com.baozistore.repository.ProdutoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    public record Cadastro(
            @NotBlank(message = "Informe o nome.") @Size(max = 255, message = "O nome deve ter até 255 caracteres.") String nome,
            @NotNull(message = "Informe o preço.") @Positive(message = "O preço deve ser maior que zero.")
            @Digits(integer = 10, fraction = 2, message = "O preço admite até 10 dígitos inteiros e 2 decimais.") BigDecimal preco,
            @NotNull(message = "Informe a disponibilidade.") Boolean estoque) {
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody Cadastro dados) {
        Produto salvo = repository.saveAndFlush(new Produto(dados.nome().strip(), dados.preco(), dados.estoque()));
        return ResponseEntity.created(URI.create("/produtos/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public List<Produto> listar() {
        return repository.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public Produto consultar(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }
}
