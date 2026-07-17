package com.purificadora.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegacyController {

    @GetMapping("/formulario-mantenimiento")
    public String redirectLegacyForm() {
        return "redirect:/mantenimiento/nuevo";
    }

    @GetMapping("/lista-mantenimentos")
    public String redirectLegacyListTypo() {
        return "redirect:/mantenimiento";
    }

    @GetMapping("/lista-mantenimientos")
    public String redirectLegacyList() {
        return "redirect:/mantenimiento";
    }

}
