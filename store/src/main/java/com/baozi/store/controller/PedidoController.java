package com.baozi.store.controller;

import com.baozi.store.model.Pedido;
import com.baozi.store.repository.PedidoRepository;
import com.baozi.store.repository.ClienteRepository;
import com.baozi.store.repository.ProdutoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido obj) {
        if (obj.getClienteId() == null || obj.getProdutoId() == null || obj.getQuantidade() == null || obj.getQuantidade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados invalidos");
        }
        if (!clientes.existsById(obj.getClienteId()) ||
                !produtos.existsById(obj.getProdutoId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente ou produto inexistente");
        }
        obj.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(obj));
    }

    @GetMapping
    public List<Pedido> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Registro vinculado a um pedido");
        }
    }
}