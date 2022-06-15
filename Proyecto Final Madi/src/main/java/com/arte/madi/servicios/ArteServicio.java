package com.arte.madi.servicios;

import com.arte.madi.entidades.Arte;
import com.arte.madi.entidades.Autor;
import com.arte.madi.entidades.Foto;
import com.arte.madi.enums.Categoria;
import com.arte.madi.repositorios.ArteRepositorio;
import com.arte.madi.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase tiene la responsabilidad de llevar adelante las funcionalidades
 * necesarias para administrar artes (consulta, creación, modificación y dar de
 * baja).
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Service
public class ArteServicio {

    @Autowired
    private ArteRepositorio arteRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private FotoServicio fotoServicio;

    /**
     * Método para registrar un arte.
     *
     * @param archivo --> foto
     * @param isbn
     * @param titulo
     * @param anio
     * @param descripcion
     * @param ejemplares
     * @param autor
     * @param editorial
     * @throws Exception
     */
    @Transactional
    public void agregarArte(MultipartFile archivo, String nombre, Integer anio, String descripcion, Integer precio, Autor autor, Categoria categoria) throws Exception {
        try {
            Arte arte = new Arte();
            // Valido los datos ingresados:
            validar(nombre, anio, descripcion, precio);
            // Seteo de atributos:
            arte.setAlta(true);
            arte.setVoto(true);
            arte.setNombre(nombre);
            arte.setAnio(anio);
            arte.setDescripcion(descripcion);
            arte.setPrecio(precio);
            arte.setAutor(autor);
            
            // Se da de alta el autor en caso de que esté dado de baja:
            if (!arte.getAutor().isAlta()) {
                autorServicio.alta(arte.getAutor().getId());
            }
           
            Foto foto = fotoServicio.guardar(archivo);
            arte.setFoto(foto);
            // Persistencia en la DB:
            arte.setCategoria(categoria);
            arteRepositorio.save(arte);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
//            throw new Exception("Error al intentar guardar el arte.");
        }
    }

    /**
     * Método para modificar un arte.
     *
     * @param id
     * @param archivo
     * @param isbn
     * @param titulo
     * @param anio
     * @param descripcion
     * @param ejemplares
     * @param autor
     * @param editorial
     * @throws Exception
     */
    @Transactional
    public void modificarArte(String id, MultipartFile archivo, String nombre, Integer anio, String descripcion, Integer precio, Autor autor, Categoria categoria) throws Exception {
        try {
            // Valido los datos ingresados:
            validar(nombre, anio, descripcion, precio);
            // Usamos el repositorio para que busque el arte cuyo id sea el pasado como parámetro.
            Optional<Arte> respuesta = arteRepositorio.findById(id);
            if (respuesta.isPresent()) { // El arte con ese id SI existe en la DB
                Arte arte = respuesta.get();
                // Seteo de atributos:
                
                arte.setNombre(nombre);
                arte.setAnio(anio);
                arte.setDescripcion(descripcion);
                arte.setPrecio(precio);
                arte.setCategoria(categoria);
                    
                
                // Seteo de Autor y Editorial:
                arte.setAutor(autor);
                if (!archivo.isEmpty()) {
                    String idFoto = null;
                    if (arte.getFoto() != null) {
                        idFoto = arte.getFoto().getId();
                    }
                    Foto foto = fotoServicio.actualizar(idFoto, archivo);
                    arte.setFoto(foto);
                }
                // Persistencia en la DB:
                arteRepositorio.save(arte);
            } else { // El arte con ese id NO existe en la DB
                throw new Exception("No existe el arte con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
//            throw new Exception("Error al intentar modificar el arte.");
        }
    }

    /**
     * El método borra el arte de la DB (no se utiliza para darlo de baja).
     *
     * @param id
     * @throws java.lang.Exception
     */
    @Transactional
    public void eliminarArte(String id) throws Exception {
        try {
            // Usamos el repositorio para que busque el arte cuyo id sea el pasado como parámetro.
            Optional<Arte> respuesta = arteRepositorio.findById(id);
            if (respuesta.isPresent()) { // El arte con ese id SI existe en la DB
                Arte arte = respuesta.get();
                // Se eliminan todos los préstamos del arte:
                //Persistencia en la DB:
                arteRepositorio.delete(arte);
            } else { // El arte con ese id NO existe en la DB
                throw new Exception("No existe Arte con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar eliminar el Arte.");
        }
    }

    /**
     * El método sirve para setear como 'false' el atributo 'alta' del arte.
     *
     * @param id
     * @throws Exception
     */
    @Transactional
    public void baja(String id) throws Exception {
        try {
            // Usamos el repositorio para que busque el arte cuyo id sea el pasado como parámetro.
            Arte arte = arteRepositorio.getById(id);
            if (arte != null) { // El arte con ese id SI existe en la DB
                // Se dan de baja los préstamos del arte:
                
                arte.setAlta(false);
                arteRepositorio.save(arte);
            } else { // El arte con ese id NO existe en la DB
                throw new Exception("No existe Obra de Arte con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar dar de baja la Obra de Arte.");
        }
    }

    /**
     * El método sirve para setear como 'true' el atributo 'alta' del arte.
     * También da de alta el autor, en caso de que esté dado de baja.
     *
     * @param id
     * @throws Exception
     */
    @Transactional
    public void alta(String id) throws Exception {
        try {
            // Usamos el repositorio para que busque el arte cuyo id sea el pasado como parámetro.
            Optional<Arte> respuesta = arteRepositorio.findById(id);
            if (respuesta.isPresent()) { // El arte con ese id SI existe en la DB
                Arte arte = respuesta.get();
                arte.setAlta(true);
                arteRepositorio.save(arte);
                // Da de alta el autor y/o editorial (en caso de que estén dados de baja):
                if (!arte.getAutor().isAlta()) {
                    autorServicio.alta(arte.getAutor().getId());
                }
                
            } else { // El arte con ese id NO existe en la DB
                throw new Exception("No existe Obra de Arte con el id indicado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar dar de alta la Obra de Arte.");
        }
    }

       

    /**
     * No se tienen en cuenta ni el Autor ni la Editorial, ya que se podrán
     * seleccionar de una lista.
     *
     * @param isbn
     * @param titulo
     * @param anio
     * @param descripcion
     * @param ejemplares
     * @throws Exception
     */
    public void validar(String nombre, Integer anio, String descripcion, Integer precio) throws Exception {

        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("Nombre no válido.");
        }
        if (anio <= 0 || anio == null) {
            throw new Exception("Año no válido.");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new Exception("La descripción es obligatoria.");
        }
        if (descripcion.length() > 255) {
            throw new Exception("La descripción no puede tener más de 200 caracteres.");
        }
        if (precio < 0 || precio == null) {
            throw new Exception("Precio ingresado no es válido.");
        }
    }

    // ------------------------------ MÉTODOS DEL REPOSITORIO ------------------------------
    /**
     *
     * @param id
     * @return
     */
    public Arte getById(String id) {
        return arteRepositorio.getById(id);
    }

    
    /**
     *
     * @param idAutor
     * @return
     */
    public List<Arte> buscarPorAutor(String idAutor) {
        return arteRepositorio.buscarPorAutor(idAutor);
    }

   
    /**
     * Sólo devuelve los artes dados de alta.
     *
     * @return
     */
    public List<Arte> findAll() {
        return arteRepositorio.findAll();
    }

    /**
     * Sólo devuelve los artes dados de baja.
     *
     * @return
     */
    public List<Arte> listarDeBaja() {
        return arteRepositorio.listarDeBaja();
    }
}
