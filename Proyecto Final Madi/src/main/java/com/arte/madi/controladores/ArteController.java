package com.arte.madi.controladores;

import com.arte.madi.entidades.Arte;
import com.arte.madi.entidades.Autor;
import com.arte.madi.enums.Categoria;
import com.arte.madi.servicios.ArteServicio;
import com.arte.madi.servicios.AutorServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
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
 * Controlador para gestionar todo lo relacionado a la entidad arte (listar,
 * registrar, modificar, dar de baja/alta, eliminar). Sólo accesible por un
 * ADMIN.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin/artes")
public class ArteController {

    
    @Autowired
    private ArteServicio arteServicio;

    @Autowired
    private AutorServicio autorServicio;

    
    /**
     * Muestra el Menú Administrativo de artes, con los datos de todos los
     * artes, autores y editoriales inyectados al modelo.
     *
     * @param model
     * @return
     */
    @GetMapping("/admin-artes")
    public String administradorArtes(ModelMap model) {
        // Datos inyectados al modelo de "admin-arte.html":
        List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Arte> artesDeBaja = arteServicio.listarDeBaja();
        model.addAttribute("artesDeBaja", artesDeBaja);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
        
        return "admin-arte.html";
    }

    /**
     * Función para registrar un arte. Se usa un "try-catch" para verificar que
     * se haya elegido un Autor y una Editorial, ya sea de la lista o se haya
     * registrado uno nuevo.
     *
     * En el caso de que alguno de los otros campos se haya dejado vacío, se
     * lanza una excepción que lo notifica.
     *
     * @param model
     * @param id
     * @param archivo
     * @param isbn
     * @param titulo
     * @param anio
     * @param descripcion
     * @param ejemplares
     * @param idAutor
     * @param nuevoAutor
     * @param idEditorial
     * @param nuevaEditorial
     * @return
     */
    @PostMapping("/registrar-arte")
    public String registrarArte(ModelMap model,HttpSession session, MultipartFile archivo, 
            @RequestParam(required = false) String nombre, Integer anio, 
            String descripcion, Integer precio, String idAutor, Categoria categoria) {
        Autor autor;
        try {
            // Seteo del Autor:
            try {
                autor = autorServicio.getById(idAutor);
               
            } catch (Exception e) {
                throw new Exception("Debe seleccionar un Autor");
            }
            // Seteo de la Editorial:
            
            arteServicio.agregarArte(archivo, nombre, anio, descripcion, precio, autor,categoria);
            // Mensaje de éxito inyectado al modelo de "admin-arte.html":
            model.put("success", "La obra " + categoria + "' fue registrado exitosamente.");
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
            model.put("artes", artes);
            List<Arte> artesDeBaja = arteServicio.listarDeBaja();
            model.put("artesDeBaja", artesDeBaja);
            List<Autor> autores = autorServicio.findAll();
            model.put("autores", autores);
            
           
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo de "error.html":
            if (e.getMessage() == null || nombre == null || anio == null || descripcion == null || precio == null || idAutor == null || categoria == null) {
                model.put("error", "Error al intentar guardar la obra de arte: faltó completar algún campo.");
            } else {
                model.put("error", "Error al intentar guardar la obra de arte: " + e.getMessage());
            }
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Arte> artesDeBaja = arteServicio.listarDeBaja();
        model.addAttribute("artesDeBaja", artesDeBaja);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
           
        }
        return "admin-arte.html";
    }

    /**
     * Función que carga la vista para modificar los datos de un arte elegido
     * previamente. Busca el arte en el repositorio por id, y lo inyecta al
     * modelo para tener todos sus datos. También se inyecta la lista de Autores
     * y Editoriales.
     *
     * @param model
     * @param idarteModif
     * @return
     */
    @GetMapping("/modificar-arte-datos/{idArteModif}")
    public String datosArte(ModelMap model,
            @PathVariable String idArteModif
    ) {
        Arte arte = arteServicio.getById(idArteModif);
        model.addAttribute("arteModif", arte);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
        return "modif-arte.html";
    }

    /**
     * Función para modificar un arte.
     *
     * ESTE MÉTODO USA EL MODEL PARA QUE APAREZCA LA ALERTA ("success") EN LA
     * MISMA PLANTILA DE "admin-arte.html", O LA ALERTA ("error") EN LA
     * PLANTILLA DE "modif-arte.html".
     *
     * @param model
     * @param id
     * @param archivo
     * @param isbn
     * @param titulo
     * @param anio
     * @param descripcion
     * @param ejemplares
     * @param idAutor
     * @param idEditorial
     * @return
     */
    @PostMapping("/modificar-arte")
    public String modificarArte(ModelMap model, @RequestParam String id, MultipartFile archivo, @RequestParam String nombre, @RequestParam Integer anio, @RequestParam String descripcion, @RequestParam Integer precio, @RequestParam String idAutor, @RequestParam Categoria categoria) {

        try {
            Autor autor = autorServicio.getById(idAutor);
           
            if (nombre.isEmpty()) {
                throw new Exception("Nombre no válido.");
            }
            arteServicio.modificarArte(id, archivo, nombre, anio, descripcion, precio, autor, categoria);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "La Obra '" + categoria + "' fue modificado exitosamente.");
            // Datos inyectados al modelo:
            List<Arte> artes = arteServicio.findAll();
            model.put("artes", artes);
            List<Arte> artesDeBaja = arteServicio.listarDeBaja();
            model.put("artesDeBaja", artesDeBaja);
            List<Autor> autores = autorServicio.findAll();
            model.put("autores", autores);
            return "admin-arte.html";
        } catch (Exception e) {
            if (e.getMessage() == null || nombre == null || anio == null || descripcion == null || precio == null || idAutor == null || categoria == null) {
                model.put("error", "Error al intentar modificar el arte: faltó completar algún campo.");
            } else {
                model.put("error", "Error al intentar modificar el arte: " + e.getMessage());
            }
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
            model.addAttribute("artes", artes);
            List<Arte> artesDeBaja = arteServicio.listarDeBaja();
            model.addAttribute("artesDeBaja", artesDeBaja);
            List<Autor> autores = autorServicio.findAll();
            model.addAttribute("autores", autores);
            model.addAttribute("categorias", Categoria.values());
            model.put("autores", autores);
            
            return "modif-arte.html";
        }
    }

    /**
     * Función para eliminar un arte. Antes de eliminarlo desde el servicio,
     * capturo el titulo en una variable para poder utilizarlo en el mensaje de
     * "success". Es una url con "path variable" (id del arte a eliminar).
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/eliminar-arte/{id}")
    public String eliminarArte(ModelMap model, @PathVariable String id) {
        try {
            String nombre = arteServicio.getById(id).getNombre().toUpperCase();
            // Con el id, llamo al método para eliminar el arte:
            arteServicio.eliminarArte(id);
            // Mensaje de éxito inyectado al modelo de "exito.html":
            model.put("success", "La Obra '" + nombre + "' fue eliminado exitosamente.");
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
            model.put("artes", artes);
            List<Arte> artesDeBaja = arteServicio.listarDeBaja();
            model.put("artesDeBaja", artesDeBaja);
            List<Autor> autores = autorServicio.findAll();
            model.put("autores", autores);
            
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar eliminar el arte: " + e.getMessage());
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
            model.put("artes", artes);
            List<Autor> autores = autorServicio.findAll();
            model.put("autores", autores);
       
        }
        return "admin-arte.html";
    }

    /**
     * Función para dar de baja un arte. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "admin-arte.html" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del arte a dar
     * de baja).
     *
     * ESTE MÉTODO USA EL MODEL PARA QUE APAREZCA LA ALERTA ("success" O
     * "error") EN LA MISMA PLANTILA DE "admin-arte.html".
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/baja/{id}")
    public String baja(ModelMap model, @PathVariable String id) {
        try {
            arteServicio.baja(id);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "La Obra '" + arteServicio.getById(id).getNombre().toUpperCase() + "' fue dado de baja exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar dar de baja el arte: " + e.getMessage());
        }
        // Datos inyectados al modelo de "admin-arte.html":
       List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Arte> artesDeBaja = arteServicio.listarDeBaja();
        model.addAttribute("artesDeBaja", artesDeBaja);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
        
        return "admin-arte.html";
    }

    /**
     * Función para dar de alta un arte. Una vez modificado el atributo "alta"
     * desde el servicio, muestra la página de "exito" o "error" con mensajes
     * inyectados al modelo. Es una url con "path variable" (id del arte a dar
     * de alta).
     *
     * ESTE MÉTODO USA EL MODEL PARA QUE APAREZCA LA ALERTA ("success" O
     * "error") EN LA MISMA PLANTILA DE "admin-arte.html".
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/alta/{id}")
    public String alta(ModelMap model, @PathVariable String id) {
        try {
            Autor autor = arteServicio.getById(id).getAutor();
            if (autor.isAlta()) {
                // Mensaje de éxito inyectado al modelo de "exito.html":
                model.put("success", "La Obra '" + arteServicio.getById(id).getNombre().toUpperCase() + "' fue dado de alta exitosamente.");
            } else {
                model.put("success", "La Obra '" + arteServicio.getById(id).getNombre().toUpperCase() + "' fue dado de alta exitosamente,"
                        + " al igual que su autor '" + autor.getNombre().toUpperCase() + "'.");
            }
            arteServicio.alta(id);
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
            model.addAttribute("artes", artes);
            List<Arte> artesDeBaja = arteServicio.listarDeBaja();
            model.addAttribute("artesDeBaja", artesDeBaja);
            List<Autor> autores = autorServicio.findAll();
            model.addAttribute("autores", autores);
            model.addAttribute("categorias", Categoria.values());
            return "admin-arte.html";
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo de "error.html":
            model.put("error", "Error al intentar dar de alta el arte: " + e.getMessage());
            // Datos inyectados al modelo de "admin-arte.html":
            List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Arte> artesDeBaja = arteServicio.listarDeBaja();
        model.addAttribute("artesDeBaja", artesDeBaja);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
            
            return "admin-arte.html";
        }
    }
    
    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/bajaDeCompra/{id}")
    public String bajaDeCompra(ModelMap model, @PathVariable String id) {
        try {
            arteServicio.bajaDeCompra(id);
            // Mensaje de éxito inyectado al modelo:
            model.put("success", "La Obra '" + arteServicio.getById(id).getNombre().toUpperCase() + "' fue dado de baja exitosamente.");
        } catch (Exception e) {
            // Mensaje de error inyectado al modelo:
            model.put("error", "Error al intentar dar de baja el arte: " + e.getMessage());
        }
        // Datos inyectados al modelo de "admin-arte.html":
       List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
        
        return "inicio.html";
    }
    
    @PreAuthorize("hasRole('USUARIO')")
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
       List<Arte> artes = arteServicio.findAll();
        model.addAttribute("artes", artes);
        List<Autor> autores = autorServicio.findAll();
        model.addAttribute("autores", autores);
        model.addAttribute("categorias", Categoria.values());
        
        return "inicio.html";
    }
}
