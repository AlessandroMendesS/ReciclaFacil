package com.example.demo.controller;

import com.example.demo.model.Carrinho;
import com.example.demo.model.Material;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CarrinhoRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @PostMapping("/adicionar")
    public ResponseEntity<Carrinho> adicionarAoCarrinho(@RequestParam Long usuarioId, @RequestParam Long materialId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Material> materialOpt = materialRepository.findById(materialId);

        if (usuarioOpt.isEmpty() || materialOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioOpt.get();
        Material material = materialOpt.get();

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuario)
                .orElse(new Carrinho(usuario, new ArrayList<>()));

        if (!carrinho.getMateriais().contains(material)) {
            carrinho.getMateriais().add(material);
        }

        carrinhoRepository.save(carrinho);
        return ResponseEntity.ok(carrinho);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Material>> getMateriaisDoCarrinho(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return carrinhoRepository.findByUsuario(usuarioOpt.get())
                .map(carrinho -> ResponseEntity.ok(carrinho.getMateriais()))
                .orElse(ResponseEntity.ok(new ArrayList<>()));
    }

    @DeleteMapping("/remover")
    public ResponseEntity<Carrinho> removerDoCarrinho(@RequestParam Long usuarioId, @RequestParam Long materialId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Material> materialOpt = materialRepository.findById(materialId);

        if (usuarioOpt.isEmpty() || materialOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioOpt.get();
        Material material = materialOpt.get();

        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuario(usuario);

        if (carrinhoOpt.isPresent()) {
            Carrinho carrinho = carrinhoOpt.get();
            carrinho.getMateriais().remove(material);
            carrinhoRepository.save(carrinho);
            return ResponseEntity.ok(carrinho);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{usuarioId}/limpar")
    public ResponseEntity<Void> limparCarrinho(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioOpt.get();
        Optional<Carrinho> carrinhoOpt = carrinhoRepository.findByUsuario(usuario);
        if (carrinhoOpt.isPresent()) {
            Carrinho carrinho = carrinhoOpt.get();
            carrinho.getMateriais().clear(); // Remove todos os materiais
            carrinhoRepository.save(carrinho);
        }
        return ResponseEntity.ok().build();
    }
}