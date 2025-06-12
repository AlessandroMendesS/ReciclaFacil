package com.example.demo.controller;

import com.example.demo.model.Material;
import com.example.demo.model.Usuario;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Material> listar() {
        return materialRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Material> cadastrar(@RequestBody Material material, @RequestParam Long usuarioId) {
        return usuarioRepository.findById(usuarioId).map(usuario -> {
            material.setUsuario(usuario);
            material.setDataCadastro(LocalDate.now());
            return ResponseEntity.ok(materialRepository.save(material));
        }).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> atualizar(@PathVariable Long id, @RequestBody Material materialAtualizado) {
        return materialRepository.findById(id)
                .map(material -> {
                    // Preserva os dados que não devem ser alterados pela edição
                    LocalDate dataCadastroOriginal = material.getDataCadastro();
                    Usuario usuarioOriginal = material.getUsuario();

                    // Atualiza apenas os campos editáveis
                    material.setTipo(materialAtualizado.getTipo());
                    material.setDescricao(materialAtualizado.getDescricao());
                    material.setQuantidade(materialAtualizado.getQuantidade());
                    // Os outros campos (unidade, localizacao, observacoes) também podem ser
                    // atualizados se vierem no corpo
                    material.setUnidade(materialAtualizado.getUnidade());
                    material.setLocalizacao(materialAtualizado.getLocalizacao());
                    material.setObservacoes(materialAtualizado.getObservacoes());

                    // Garante que o usuário e a data de cadastro não sejam apagados
                    material.setDataCadastro(dataCadastroOriginal);
                    material.setUsuario(usuarioOriginal);

                    return ResponseEntity.ok(materialRepository.save(material));
                })
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 se o material não for encontrado
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        materialRepository.deleteById(id);
    }
}