package com.arte.madi.controladores;


import com.arte.madi.entidades.Arte;
import com.arte.madi.entidades.Autor;
import com.arte.madi.entidades.Usuario;
import com.arte.madi.servicios.ArteServicio;
import com.arte.madi.servicios.UsuarioServicio;
import com.arte.madi.servicios.AutorServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para gestionar las fotos (de portada de libros y de perfil de
 * usuarios).
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Controller
@RequestMapping("/foto")
public class FotoController {

   

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private ArteServicio arteServicio;
   /**
     * Función que devuelve la foto de perfil de un usuario con PathVariable.
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id) throws Exception {
        try {
            /*Buscamos el Usuario por id, luego almacenamos el contenido
            de la foto en un arreglo de bytes:*/
            Usuario usuario = usuarioServicio.getById(id);
            if (usuario.getFoto() == null) {
                throw new Exception("El Usuario no tiene una foto de perfil.");
            }
            byte[] foto = usuario.getFoto().getContenido();
            /*Para poder mostrar la foto con ResponseEntity, hay que crear
            los headers, para indicarle que el tipo de contenido será una
            imagen JPEG:*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            /*Ahora usamos esos headers para el return; el tercer parámetro del
            ResponseEntity es el estado en el que se termina el proceso de
            petición http (código 200 en este caso):*/
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/autor/{id}")
    public ResponseEntity<byte[]> fotoAutor(@PathVariable String id) throws Exception {
        try {
            /*Buscamos el Usuario por id, luego almacenamos el contenido
            de la foto en un arreglo de bytes:*/
            Autor autor = autorServicio.getById(id);
            if (autor.getFoto() == null) {
                throw new Exception("El Autor no tiene una foto de perfil.");
            }
            byte[] foto = autor.getFoto().getContenido();
            /*Para poder mostrar la foto con ResponseEntity, hay que crear
            los headers, para indicarle que el tipo de contenido será una
            imagen JPEG:*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            /*Ahora usamos esos headers para el return; el tercer parámetro del
            ResponseEntity es el estado en el que se termina el proceso de
            petición http (código 200 en este caso):*/
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/arte/{id}")
    public ResponseEntity<byte[]> fotoArte(@PathVariable String id) throws Exception {
        try {
            /*Buscamos el Usuario por id, luego almacenamos el contenido
            de la foto en un arreglo de bytes:*/
            Arte arte = arteServicio.getById(id);
            if (arte.getFoto() == null) {
                throw new Exception("La Obra de Arte no tiene una foto.");
            }
            byte[] foto = arte.getFoto().getContenido();
            /*Para poder mostrar la foto con ResponseEntity, hay que crear
            los headers, para indicarle que el tipo de contenido será una
            imagen JPEG:*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            /*Ahora usamos esos headers para el return; el tercer parámetro del
            ResponseEntity es el estado en el que se termina el proceso de
            petición http (código 200 en este caso):*/
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception ex) {
            Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
