package com.arte.madi.repositorios;


import com.arte.madi.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * (FotoRepositorio) debe contener los métodos necesarios para
 * guardar/actualizar fotos en la base de datos o dar de baja según corresponda.
 * Extiende de JpaRepository: será un repositorio de Foto con la Primary Key de
 * tipo String.
 *
 * Los métodos save(), findById() y delete() se implementan por JpaRepository.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}
