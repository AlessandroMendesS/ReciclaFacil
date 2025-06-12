package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany
    private List<Material> materiais;

    public Carrinho() {
    }

    public Carrinho(Usuario usuario, List<Material> materiais) {
        this.usuario = usuario;
        this.materiais = materiais;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }
}