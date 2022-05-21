package com.arte.madi.repositorios;


import com.arte.madi.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * (AutorRepositorio) debe contener los métodos necesarios para
 * guardar/actualizar un cliente en la base de datos, realizar consultas o dar
 * de baja según corresponda. Extiende de JpaRepository: será un repositorio de
 * Autor con la Primary Key de tipo String.
 *
 * Los métodos save(), findById() y delete() se implementan por JpaRepository.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {

    // Método que devuelve el Autor buscado por su nombre:
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Autor buscarPorNombre(@Param("nombre") String nombre);
    
    // Método que devuelve todos los autores, ordenados alfabéticamente.
    @Query("SELECT a FROM Autor a ORDER BY a.nombre ASC")
    @Override
    public List<Autor> findAll();
}
