package br.com.baozistore.controller;

import br.com.baozistore.model.Cliente;
import br.com.baozistore.repository.ClienteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteRepository repository;

    public ClienteController(ClienteRepository repository) {
        this.repository = repository;
    }

    public record Cadastro(
            @NotBlank(message = "Informe o nome.") @Size(max = 255, message = "O nome deve ter até 255 caracteres.") String nome,
            @NotNull(message = "Informe a data de cadastro.") LocalDate clienteDesde) {
    }

    @PostMapping
    public ResponseEntity<Cliente> criar(@Valid @RequestBody Cadastro dados) {
        Cliente salvo = repository.saveAndFlush(new Cliente(dados.nome().strip(), dados.clienteDesde()));
        return ResponseEntity.created(URI.create("/clientes/" + salvo.getId())).body(salvo);
    }

    @GetMapping
    public List<Cliente> listar() {
        return repository.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public Cliente consultar(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        repository.delete(consultar(id));
        return ResponseEntity.noContent().build();
    }
}
