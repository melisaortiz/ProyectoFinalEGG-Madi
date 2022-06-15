package com.arte.madi.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

/**
 * La entidad foto modela los datos de las fotos que se utilicen en el proyecto.
 * Estas pueden ser foto de perfil de cada cliente, o foto de la portada de cada
 * arte.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Entity
public class Foto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String mime; // Asigna el formato del archivo de la foto.

    // "@Lob" Identifica que el tipo de dato es pesado.
    // "@Basic(fetch = FetchType.LAZY)" indica que cargue el contenido sólo cuando lo pidamos.
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] contenido; // Guarda el contenido de la foto.

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the mime
     */
    public String getMime() {
        return mime;
    }

    /**
     * @param mime the mime to set
     */
    public void setMime(String mime) {
        this.mime = mime;
    }

    /**
     * @return the contenido
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

}
