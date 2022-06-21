package com.arte.madi.controladores;

import com.arte.madi.entidades.Arte;
import com.arte.madi.entidades.Autor;
import com.arte.madi.enums.Categoria;
import com.arte.madi.servicios.ArteServicio;
import com.arte.madi.servicios.AutorServicio;
import com.arte.madi.servicios.UsuarioServicio;
import java.util.List;
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
    private ArteServicio arteServicio;

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
    public String index() {
        return "index.html";
    }
    
       @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos.");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login.html";
    }
    
         @GetMapping("/tienda")
         public String tienda(ModelMap model) {

            List<Autor> autores = autorServicio.findAll();
             model.addAttribute("autores", autores);
             model.addAttribute("autorSelected", null);
             List <Arte> artes = arteServicio.findAll();
             model.addAttribute("artes", artes);

           return "tienda.html";
    }
         
             
         @GetMapping("/tiendas")
         public String tiendas(ModelMap model , String idAutor) {
             model.addAttribute("autorSelected", autorServicio.getById(idAutor));
             List<Autor> autores = autorServicio.findAll();
             model.addAttribute("autores", autores);
             List <Arte> artes = arteServicio.buscarPorAutor(idAutor);
             model.addAttribute("artes", artes);

           return "tienda.html";
    }
         
    
         @GetMapping("/contacto")
    public String contacto() {
        return "contacto.html";
    }
    
         @GetMapping("/faqs")
    public String faqs() {
        return "faqs.html";
    }
    
    @GetMapping("/carrito")
    public String carrito(ModelMap model) {
        List<Arte> artesDeCompra = arteServicio.listarDeCompra();
        model.addAttribute("artesDeCompra", artesDeCompra);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("autorSelected", null);
        List <Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        return "carrito.html";
    }
    
    
    /**
     * Vista principal para los usuarios logueados. Para los ADMIN se ve el Menú
     * Administrativo (acceso a Dashboard, Gestión de artes/Autores/Editoriales
     * y Gestión de Préstamos); para los USUARIOS se ve la lista de todos los
     * artes activos disponibles para solicitar préstamos, además del cuadro
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
        List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);

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
            return "login.html";
        }
        model.put("success", "¡Registro exitoso! Ahora puede iniciar sesión.");
        return "login.html";
    }

    /**
     * Método para filtrar artes por Autor en la vista para USUARIO.
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
    
    @GetMapping("/altaDeCompra/{id}")
    public String altaDeCompra(ModelMap model, @PathVariable String id) {
        try {
            arteServicio.altaDeCompra(id);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "La Obra '" + arteServicio.getById(id).getNombre().toUpperCase() + "' fue dado de alta exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar dar de alta el arte: " + e.getMessage());
        }
        // Datos inyectados al modelo de "admin-arte.html":
             
             List<Autor> autores = autorServicio.findAll();
             model.addAttribute("autores", autores);
             List <Arte> artes = arteServicio.findAll();
             model.addAttribute("artes", artes);
        
        return "tienda.html";
    }
}



