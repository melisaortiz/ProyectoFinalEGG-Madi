package com.arte.madi.servicios;

import com.arte.madi.entidades.Autor;
import com.arte.madi.entidades.Foto;
import com.arte.madi.enums.Provincias;
import com.arte.madi.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase tiene la responsabilidad de llevar adelante las funcionalidades
 * necesarias para administrar autores (consulta, creación, modificación y dar
 * de baja).
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;

    
    /**
     * Método para registrar un autor.
     *
     * @param nombre
     * @throws Exception
     */
    @Transactional
    public void agregarAutor(MultipartFile archivo, String nombre, 
                             String descripcion, String redSocial,
                             Provincias provincias) throws Exception {
        try {
            Autor autor = new Autor();
            // Valido los datos ingresados:
            validar(nombre, descripcion, redSocial);
            // Seteo de atributos:
            autor.setAlta(true);
            autor.setNombre(nombre);
            autor.setDescripcion(descripcion);
            autor.setRedSocial(redSocial);
            autor.setProvincias(provincias);
            Foto foto = fotoServicio.guardar(archivo);
            autor.setFoto(foto);
            
            // Persistencia en la DB:
            autorRepositorio.save(autor);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método para modificar un autor.
     *
     * @param id
     * @param nombre
     * @throws Exception
     */
    @Transactional
    public void modificarAutor(String id, MultipartFile archivo, String nombre, 
                             String descripcion, String redSocial,
                             Provincias provincias) throws Exception {
        try {
            // Valido los datos ingresados:
            validar(nombre, descripcion, redSocial);
            Optional<Autor> respuesta = autorRepositorio.findById(id);
            if (respuesta.isPresent()) { // El autor con ese id SI existe en la DB
                Autor autor = respuesta.get();
                // Seteo de atributos:
                autor.setNombre(nombre);
                autor.setDescripcion(descripcion);
                autor.setRedSocial(redSocial);
                autor.setProvincias(provincias);
                Foto foto = fotoServicio.guardar(archivo);
                autor.setFoto(foto);
                // Persistencia en la DB:
                autorRepositorio.save(autor);
            } else { // El autor con ese id NO existe en la DB
                throw new Exception("No existe el autor con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

   

// ------------------------------ MÉTODOS DEL REPOSITORIO ------------------------------
 
    /**
     *
     * @param nombre
     * @return
     */
    public Autor buscarPorNombre(String nombre) {
        return autorRepositorio.buscarPorNombre(nombre);
    }

    /**
     *
     * @return
     */
    public List<Autor> findAll() {
        return autorRepositorio.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Autor getById(String id) {
        return autorRepositorio.getById(id);
    }
    
     @Transactional
    public void eliminarAutor(String id) throws Exception {
        try {
            Autor autor = autorRepositorio.getById(id);
            if (autor != null) { // El autor con ese id SI existe en la DB
                
                autorRepositorio.delete(autor);
                
            } else { // El autor con ese id NO existe en la DB
                throw new Exception("No existe el autor con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @Transactional
    public void baja(String id) throws Exception {
        try {
            Autor autor = autorRepositorio.getById(id);
            if (autor != null) { // El autor con ese id SI existe en la DB
                // Dar de baja todos sus libros:
               autor.setAlta(false);
                // Persistencia en la DB:
                autorRepositorio.save(autor);
            } else { // El autor con ese id NO existe en la DB
                throw new Exception("No existe el autor con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar dar de baja el Autor.");
        }
    }
    
    @Transactional
    public void alta(String id) throws Exception {
        try {
            Optional<Autor> respuesta = autorRepositorio.findById(id);
            if (respuesta.isPresent()) { // El autor con ese id SI existe en la DB
                Autor autor = respuesta.get();
                autor.setAlta(true);
                // Persistencia en la DB:
                autorRepositorio.save(autor);
                // Dar de alta todos sus libros:
              
            } else { // El autor con ese id NO existe en la DB
                throw new Exception("No existe el autor con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar dar de alta el Autor.");
        }
    }
    
    public void validar(String nombre, String descripcion, String redSocial) throws Exception {

        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("Nombre no válido.");
        }
       
        if (descripcion == null || descripcion.isEmpty()) {
            throw new Exception("La descripción es obligatoria.");
        }
        if (descripcion.length() > 255) {
            throw new Exception("La descripción no puede tener más de 200 caracteres.");
        }
        if (redSocial == null || redSocial.isEmpty()) {
            throw new Exception("Precio ingresado no es válido.");
        }
    }
}
