package com.arte.madi.controladores;


import com.arte.madi.entidades.Usuario;
import com.arte.madi.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controlador para gestionar todas las funciones para el rol de ADMIN en
 * relación a Usuarios
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioServicio usuarioServicio;

   //... @Autowired
    //...private LibroServicio libroServicio;

    /**
     * Trae la vista del Dashboard. Incluye una tabla (para lo cual se pasan
     * todos los usuarios a través del model) con la lista de usuarios y
     * distintas opciones para gestionarlos.
     *
     * @param model
     * @return
     */
    @GetMapping("/dashboard")
    public String homeAdmin(ModelMap model) {
        // Pasamos la lista de todos los usuarios para la tabla del dashboard:
        List<Usuario> usuariosActivos = usuarioServicio.buscarActivos();
        model.addAttribute("usuariosActivos", usuariosActivos);
        List<Usuario> usuariosInactivos = usuarioServicio.buscarInactivos();
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        return "admin.html";
    }

    /**
     * Método para eliminar un usuario a partir de un @PathVariable
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(ModelMap model, @PathVariable String id) {
        try {
            usuarioServicio.eliminar(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al intentar eliminar el usuario.");
            return "admin.html";
        }
    }

    /**
     * Método para dar de alta un usuario a partir de un @PathVariable.
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/habilitar/{id}")
    public String habilitar(ModelMap model, @PathVariable String id) {
        try {
            usuarioServicio.habilitar(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al intentar dar de alta al usuario.");
            return "admin.html";
        }
    }

    /**
     * Método para dar de baja un usuario a partir de un @PathVariable.
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitar(ModelMap model, @PathVariable String id) {
        try {
            usuarioServicio.deshabilitar(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al intentar dar de baja al usuario.");
            return "admin.html";
        }
    }

    /**
     * Método para cambiar el rol de un usuario a partir de un @PathVariable
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/cambiar-rol/{id}")
    public String cambiarRol(ModelMap model, @PathVariable String id) {
        try {
            usuarioServicio.cambiarRol(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Error al intentar modificar el rol.");
            return "admin.html";
        }
    }

    

    
}
