package com.arte.madi.repositorios;


import com.arte.madi.entidades.Arte;
import com.arte.madi.enums.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * (arteRepositorio) debe contener los métodos necesarios para
 * guardar/actualizar artes en la base de datos, realizar consultas o dar de
 * baja según corresponda. Extiende de JpaRepository: será un repositorio de
 * arte con la Primary Key de tipo String.
 *
 * Los métodos save(), findById() y delete() se implementan por JpaRepository.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Repository
public interface ArteRepositorio extends JpaRepository<Arte, String> {

    // Método que devuelve el Arte/s vinculado a un Autor:
    @Query("SELECT art FROM Arte art WHERE art.autor.id = :id")
    public List<Arte> buscarPorAutor(@Param("id") String id);

    
    // Método que sólo devuelve el arte dados de alta.
    @Override
    @Query("SELECT art FROM Arte art WHERE art.alta IS true ORDER BY art.nombre ASC")
    public List<Arte> findAll();
    
    // Método que sólo devuelve las artes de baja.
    @Query("SELECT art FROM Arte art WHERE art.alta IS false ORDER BY art.nombre ASC")
    public List<Arte> listarDeBaja();
    
    @Query("SELECT art FROM Arte art WHERE art.compra IS true ORDER BY art.nombre ASC")
    public List<Arte> listarDeCompra();
    
    @Query ("SELECT SUM(art.precio) FROM Arte art WHERE art.compra IS true")
    public List<Long> sumaCarrito();
    
    @Query("SELECT art FROM Arte art WHERE art.categoria = :categoria")
    public List<Arte> buscarPorCategoria(@Param("categoria") Categoria categoria);
    
}