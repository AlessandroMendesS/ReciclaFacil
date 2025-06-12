package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        try {
            // Verifica se o email já existe
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Email já cadastrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Verifica se o username já existe
            if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Nome de usuário já existe");
                return ResponseEntity.badRequest().body(response);
            }

            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao cadastrar usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(usuario.getEmail());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuarioEncontrado = usuarioOpt.get();
                if (usuarioEncontrado.getSenha().equals(usuario.getSenha())) {
                    // Remove a senha antes de enviar a resposta
                    usuarioEncontrado.setSenha(null);
                    return ResponseEntity.ok(usuarioEncontrado);
                }
            }
            
            Map<String, String> response = new HashMap<>();
            response.put("error", "Email ou senha inválidos");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao fazer login: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 