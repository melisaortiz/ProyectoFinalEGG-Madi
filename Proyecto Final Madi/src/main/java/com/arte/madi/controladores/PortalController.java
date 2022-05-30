package com.arte.madi.controladores;

import com.arte.madi.entidades.Autor;
import com.arte.madi.servicios.AutorServicio;
import com.arte.madi.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador para las vistas de login, registro e inicio de los usuarios.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    

    @Autowired
    private AutorServicio autorServicio;

    /**
     * Devuelve el index. Aquí están las opciones para registrarse o iniciar
     * sesión.
     *
     * @param error
     * @param logout
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos.");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "index.html";
    }

    /**
     * Vista principal para los usuarios logueados. Para los ADMIN se ve el Menú
     * Administrativo (acceso a Dashboard, Gestión de Libros/Autores/Editoriales
     * y Gestión de Préstamos); para los USUARIOS se ve la lista de todos los
     * libros activos disponibles para solicitar préstamos, además del cuadro
     * para filtrar la búsqueda por autor.
     *
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap model) {
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("autorSelected", null);
        return "inicio.html";
    }

    /**
     * Método para registrar un usuario en la base de datos.
     *
     * @param model
     * @param archivo
     * @param nombre
     * @param apellido
     * @param dni
     * @param telefono
     * @param mail
     * @param clave
     * @param clave2
     * @return
     */
    @PostMapping("/registrar")
    public String registrar(ModelMap model, MultipartFile archivo, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) {
        try {
            usuarioServicio.registrar(archivo, nombre, apellido, dni, telefono, mail, clave, clave2);
        } catch (Exception e) {
            // Para mostrar en el formulario algún error por datos inválidos.
            model.put("errorReg", "Error al intentar registrarse: " + e.getMessage() + " Vuelva a intentarlo.");
            /**
             * En caso de que se muestre un error, con las siguientes lineas
             * guardamos los campos que ya había ingresado el usuario antes del
             * error, para que no deba cargarlos nuevamente. En el html, éstos
             * datos se representan con el "th:text" al final de la etiqueta del
             * input.
             */
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("dni", dni);
            model.put("telefono", telefono);
            model.put("mail", mail);
            model.put("archivo", archivo);
            return "index.html";
        }
        model.put("success", "¡Registro exitoso! Ahora puede iniciar sesión.");
        return "index.html";
    }

    /**
     * Método para filtrar Libros por Autor en la vista para USUARIO.
     *
     * @param model
     * @param idAutor
     * @return
     */
    @GetMapping("/autor")
    public String autores(ModelMap model, String idAutor) {
        model.addAttribute("autorSelected", autorServicio.getById(idAutor));
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
       
        return "inicio.html";
    }
}
