package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @GetMapping("/entrar")
    public String entrar() {
        return "entrar";
    }

    @GetMapping("/carrinho")
    public String carrinho() {
        return "carrinho";
    }

    @GetMapping("/cadastrar-material")
    public String cadastrarMaterial() {
        return "cadastrar-material";
    }

    @GetMapping("/relatorio")
    public String relatorio() {
        return "relatorio";
    }

    @GetMapping("/mapa")
    public String mapa() {
        return "mapa";
    }
}