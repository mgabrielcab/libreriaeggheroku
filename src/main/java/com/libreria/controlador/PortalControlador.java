package com.libreria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //Indica que es un componente controlador
@RequestMapping("/") //configura la url que escucha la url
public class PortalControlador {

    @GetMapping("/") //responde a la url / devolviendo la pagina Index
    public String index() {
        return "index.html";
    }

}
