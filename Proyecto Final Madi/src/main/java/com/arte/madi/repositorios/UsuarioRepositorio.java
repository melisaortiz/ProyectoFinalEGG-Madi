package com.arte.madi.repositorios;


import com.arte.madi.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * (UsuarioRepositorio) debe contener los métodos necesarios para
 * guardar/actualizar usuarios en la base de datos, realizar consultas o dar de
 * baja según corresponda. Extiende de JpaRepository: será un repositorio de
 * Usuario con la Primary Key de tipo String.
 *
 * Los métodos save(), findById() y delete() se implementan por JpaRepository.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

    // Devuelve un Usuario buscado por su mail.
    @Query("SELECT u FROM Usuario u WHERE u.mail = :mail")
    public Usuario buscarPorMail(@Param("mail") String mail);

    // Devuelve una Lista con Usuarios dados de alta.
    @Query("SELECT u FROM Usuario u WHERE u.baja IS null")
    public List<Usuario> buscarActivos();

    // Devuelve una Lista con Usuarios dados de baja.
    @Query("SELECT u FROM Usuario u WHERE u.baja IS NOT null")
    public List<Usuario> buscarInactivos();
}
