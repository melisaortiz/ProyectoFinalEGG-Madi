package com.arte.madi.controladores;


import com.arte.madi.entidades.Usuario;
import com.arte.madi.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Acceso para todo tipo de usuario para modificar los datos de su perfil.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    /**
     * Precarga datos con la información del usuario que lo solicita.
     *
     * @param model
     * @param idUsuarioModif
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/modificar-usuario-datos/{idUsuarioModif}")
    public String datosUsuario(ModelMap model, @PathVariable String idUsuarioModif) {
        Usuario usuario = usuarioServicio.getById(idUsuarioModif);
        model.addAttribute("usuarioModif", usuario);
        return "modif-usuario.html";
    }

    /**
     * Devuelve la vista de modificación de datos con la información precargada
     * del usuario que lo solicita.
     *
     * @param session
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        /*Securización para evitar que el perfil pueda ser editado sólo por el
        usuario logueado, si es que se corresponde su id:*/
        if (login == null) {
            return "redirect:/";
        }
        if (!login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.getById(id);
            model.addAttribute("usuarioModif", usuario);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "modif-usuario.html";
    }

    /**
     * Método para actualizar la información del usuario que lo solicite.
     *
     * @param model
     * @param session
     * @param archivo
     * @param id
     * @param nombre
     * @param apellido
     * @param dni
     * @param telefono
     * @param mail
     * @param clave
     * @param clave2
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap model, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) {
        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            usuario = usuarioServicio.getById(id);
            usuarioServicio.modificar(id, archivo, nombre, apellido, dni, telefono, mail, clave, clave2);
            session.setAttribute("usuariosession", usuario);
        } catch (Exception e) {
            model.put("error", e.getMessage());
            model.put("perfil", usuario);
            return "perfil.html";
        }
        return "redirect:/inicio";
    }
}
