package com.arte.madi.entidades;

import com.arte.madi.enums.Categoria;
import com.arte.madi.enums.Provincias;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;


/**
 * La entidad autor modela los autores de las obras de arte.
 */
@Entity
public class Autor {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String descripcion;
    private String redSocial;
    private boolean alta;

    @OneToOne
    private Foto foto;
    @Enumerated(EnumType.STRING)
    private Provincias provincias;

    public Autor() {
    }

    public Autor(String nombre, String descripcion, String redSocial, boolean alta, Foto foto, Provincias provincias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.redSocial = redSocial;
        this.alta = alta;
        this.foto = foto;
        this.provincias = provincias;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Provincias getProvincias() {
        return provincias;
    }

    public void setProvincias(Provincias provincias) {
        this.provincias = provincias;
    }

    @Override
    public String toString() {
        return "Autor{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", redSocial=" + redSocial + ", alta=" + alta + ", foto=" + foto + ", provincias=" + provincias + '}';
    }

    

}

