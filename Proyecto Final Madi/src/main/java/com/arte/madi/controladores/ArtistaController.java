/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arte.madi.controladores;

import com.arte.madi.entidades.Autor;
import com.arte.madi.enums.Provincias;
import com.arte.madi.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author lgsan
 */
@Controller
@RequestMapping("/artistas")
public class ArtistaController {
    




    @Autowired
    private AutorServicio autorServicio;

    /**
     * Muestra el Menú Administrativo de Autores, con los datos de todos los
     * autores inyectados al modelo.
     *
     * @param model
     * @return
     */
    @GetMapping("/mostrar-artistas")
    public String mostrarArtistas(ModelMap model) {
        List<Autor> autores = autorServicio.findAll();
        model.put("autores", autores);
        model.addAttribute("provincias", Provincias.values());
        return "artistas.html";
    }
}