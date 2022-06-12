package com.arte.madi.controladores;


import com.arte.madi.entidades.Autor;
import com.arte.madi.enums.Provincias;
import com.arte.madi.servicios.AutorServicio;
import java.util.List;
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
 * Controlador para gestionar la entidad Autor (listar, registrar, modificar,
 * dar de baja/alta, eliminar).
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/autores")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    /**
     * Muestra el Menú Administrativo de Autores, con los datos de todos los
     * autores inyectados al modelo.
     *
     * @param model
     * @return
     */
    @GetMapping("/admin-autores")
    public String administradorAutores(ModelMap model) {
        List<Autor> autores = autorServicio.findAll();
        
        model.addAttribute("autores", autores);
        model.addAttribute("provincias", Provincias.values());
        return "admin-autor.html";
    }

    /**
     * Función para registrar un autor. Una vez seteados los atributos desde el
     * servicio, muestra la página de "admin-autor" con mensajes inyectados al
     * modelo.
     *
     * @param model
     * @param id
     * @param nombre
     * @return
     */
    @PostMapping("/registrar-autor")
    public String registrarAutor(ModelMap model,HttpSession session, MultipartFile archivo,
                                 @RequestParam(required = false) String nombre,
                                 String descripcion, String redSocial, Provincias provincia) {

        try {
            autorServicio.agregarAutor(archivo,nombre,descripcion,redSocial, provincia);
            // Mensaje de éxito inyectado al modelo de "admin-autor.html":
            model.put("success", "El autor '" + nombre.toUpperCase() + "' fue registrado exitosamente.");
            // Datos inyectados al modelo de "admin-autor.html":
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo de "admin-autor.html":
            if (e.getMessage() == null || nombre == null) {
                model.put("error", "Error al intentar guardar el autor: faltó completar algún campo.");
            } else {
                model.put("error", "Error al intentar guardar el autor: " + e.getMessage());
            }
        }
        List<Autor> autores = autorServicio.findAll();
        model.put("autores", autores);
        return "admin-autor.html";
    }

    /**
     * Función que carga la vista para modificar los datos de un autor elegido
     * previamente. Busca el autor en el repositorio por id, y lo inyecta al
     * modelo para tener todos sus datos. Es una url con "path variable" (id del
     * autor a modificar).
     *
     * @param model
     * @param idAutorModif
     * @return
     */
    @GetMapping("/modificar-autor-datos/{idAutorModif}")
    public String datosAutor(ModelMap model, @PathVariable String idAutorModif) {
        Autor autor = autorServicio.getById(idAutorModif);
        model.put("autorModif", autor);
        model.addAttribute("provincias", Provincias.values());
        return "modif-autor.html";
    }

    /**
     * Función para modificar un autor.
     *
     * ESTE MÉTODO USA EL MODEL PARA QUE APAREZCA LA ALERTA ("success") EN LA
     * MISMA PLANTILA DE "admin-libro.html", O LA ALERTA ("error") EN LA
     * PLANTILLA DE "modif-libro.html".
     *
     * @param model
     * @param id
     * @param nombre
     * @return
     */
    @PostMapping("/modificar-autor")
    public String modificarAutor(ModelMap model, MultipartFile archivo, @RequestParam String id, 
            @RequestParam String nombre, String descripcion, String redSocial, Provincias provincia)  {

        try {
            autorServicio.modificarAutor(id, archivo, nombre, descripcion, redSocial, provincia);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "El autor '" + nombre.toUpperCase() + "' fue modificado exitosamente.");
            // Datos inyectados al modelo de "admin-autor.html":
            List<Autor> autores = autorServicio.findAll();
            model.put("autores", autores);
            return "admin-autor.html";
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            Autor autor = autorServicio.getById(id);
            model.put("autorModif", autor);
            model.put("error", "Error al intentar modificar el autor: " + e.getMessage());
            return "modif-autor.html";
        }
    }

    /**
     * Función para eliminar un autor. Antes de eliminarlo desde el servicio,
     * capturo el nombre en una variable para poder utilizarlo en la página
     * "admin-autor" con mensajes inyectados al modelo. Es una url con "path
     * variable" (id del autor a eliminar).
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/eliminar-autor/{id}")
    public String eliminarAutor(ModelMap model, @PathVariable String id) {
        try {
            String nombre = autorServicio.getById(id).getNombre().toUpperCase();
            // Con el id, llamo al método para eliminar el autor:
            autorServicio.eliminarAutor(id);
            model.put("success", "El autor '" + nombre + "' fue eliminado exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar eliminar el autor: " + e.getMessage());
        }
        // Datos inyectados al modelo:
        List<Autor> autores = autorServicio.findAll();
        model.put("autores", autores);
        return "admin-autor.html";
    }

    /**
     * Función para dar de baja un autor. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "admin-autor" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del autor a dar
     * de baja).
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/baja/{id}")
    public String baja(ModelMap model, @PathVariable String id) {
        try {
            autorServicio.baja(id);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "El autor '" + autorServicio.getById(id).getNombre().toUpperCase() + "' fue dado de baja exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar dar de baja el autor: " + e.getMessage());
        }
        // Datos inyectados al modelo de "admin-autor.html":
        List<Autor> autores = autorServicio.findAll();
        model.put("autores", autores);
        return "admin-autor.html";
    }

    /**
     * Función para dar de alta un autor. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "admin-autor" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del autor a dar
     * de alta).
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/alta/{id}")
    public String alta(ModelMap model, @PathVariable String id) {
        try {
            autorServicio.alta(id);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "El autor '" + autorServicio.getById(id).getNombre().toUpperCase() + "' fue dado de alta exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar dar de alta el autor: " + e.getMessage());
        }
        // Datos inyectados al modelo de "admin-autor.html":
        List<Autor> autores = autorServicio.findAll();
        model.put("autores", autores);
        return "admin-autor.html";
    }

    // ----------------------------DESDE ACÁ COMIENZAN LOS MÉTODOS DESECHADOS ---------------------------
//    @PostMapping("/registrar-autor")
//    public String registrarAutor(ModelMap model, @RequestParam(required = false) String id, @RequestParam String nombre) {
//
//        try {
//            autorServicio.agregarAutor(nombre);
//            // Mensaje de éxito inyectado al modelo de "exito.html":
//            model.put("msg", "El autor '" + nombre.toUpperCase() + "' fue registrado exitosamente.");
//            model.put("entity", "Autores");
//            return "exito.html";
//        } catch (Exception e) {
//            // Mensaje de error inyectado al modelo de "error.html":
//            model.put("msg", "Error al intentar guardar el autor: " + e.getMessage());
//            model.put("entity", "Autores");
//            return "error.html";
//        }
//    }
    /**
     * Función para modificar un autor. Una vez seteados los atributos desde el
     * servicio, muestra la página de "exito" o "error" con mensajes inyectados
     * al modelo.
     */
    //    @PostMapping("/modificar-autor")
//    public String modificarAutor(ModelMap model, @RequestParam String id, @RequestParam String nombre) {
//
//        try {
//            autorServicio.modificarAutor(id, nombre);
//            // Mensaje de éxito inyectado al modelo de "exito.html":
//            model.put("msg", "El autor '" + nombre.toUpperCase() + "' fue modificado exitosamente.");
//            model.put("entity", "Autores");
//            return "exito.html";
//        } catch (Exception e) {
//            // Mensaje de error inyectado al modelo de "error.html":
//            model.put("msg", "Error al intentar modificar el autor: " + e.getMessage());
//            model.put("entity", "Autores");
//            return "error.html";
//        }
//    }
    /**
     * Función para eliminar un autor. Antes de eliminarlo desde el servicio,
     * capturo el nombre en una variable para poder utilizarlo en las páginas de
     * "exito" o "error" con mensajes inyectados al modelo. Es una url con "path
     * variable" (id del autor a eliminar).
     */
//    @GetMapping("/eliminar-autor/{id}")
//    public String eliminarAutor(ModelMap model, @PathVariable String id) {
//        try {
//            String nombre = autorRepositorio.getById(id).getNombre().toUpperCase();
//            // Con el id, llamo al método para eliminar el autor:
//            autorServicio.eliminarAutor(id);
//            // Mensaje de éxito inyectado al modelo de "exito.html":
//            model.put("msg", "El autor '" + nombre + "' fue eliminado exitosamente.");
//            model.put("entity", "Autores");
//            return "exito.html";
//        } catch (Exception e) {
//            // Mensaje de error inyectado al modelo de "error.html":
//            model.put("msg", "Error al intentar eliminar el autor: " + e.getMessage());
//            model.put("entity", "Autores");
//            return "error.html";
//        }
//    }
    /**
     * Función para dar de baja un autor. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "exito" o "error" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del autor a dar
     * de baja).
     */
//    @GetMapping("/baja/{id}")
//    public String baja(ModelMap model, @PathVariable String id) {
//        try {
//            autorServicio.baja(id);
//            // Mensaje de éxito inyectado al modelo de "exito.html":
//            model.put("msg", "El autor '" + autorRepositorio.getById(id).getNombre().toUpperCase() + "' fue dado de baja exitosamente.");
//            model.put("entity", "Autores");
//            return "exito.html";
//        } catch (Exception e) {
//            // Mensaje de error inyectado al modelo de "error.html":
//            model.put("msg", "Error al intentar dar de baja el autor: " + e.getMessage());
//            model.put("entity", "Autores");
//            return "error.html";
//        }
//    }
    /**
     * Función para dar de alta un autor. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "exito" o "error" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del autor a dar
     * de alta).
     */
//    @GetMapping("/alta/{id}")
//    public String alta(ModelMap model, @PathVariable String id) {
//        try {
//            autorServicio.alta(id);
//            // Mensaje de éxito inyectado al modelo de "exito.html":
//            model.put("msg", "El autor '" + autorRepositorio.getById(id).getNombre().toUpperCase() + "' fue dado de alta exitosamente.");
//            model.put("entity", "Autores");
//            return "exito.html";
//        } catch (Exception e) {
//            // Mensaje de error inyectado al modelo de "error.html":
//            model.put("msg", "Error al intentar dar de alta el autor: " + e.getMessage());
//            model.put("entity", "Autores");
//            return "error.html";
//        }
//    }
}
