package com.arte.madi.entidades;

import com.arte.madi.enums.Categoria;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;


/**
 * La entidad arte modela los artes que están disponibles en la biblioteca
 * para ser prestados. En esta entidad, el atributo “ejemplares” contiene la
 * cantidad total de ejemplares de ese arte, mientras que el atributo
 * “ejemplaresPrestados” contiene cuántos de esos ejemplares se encuentran
 * prestados en este momento y el atributo “ejemplaresRestantes” contiene
 * cuántos de esos ejemplares quedan para prestar.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Entity
public class Arte {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String nombre;
    private String descripcion;
    private Integer anio;    
    private Integer precio;
    private boolean voto;
    private boolean alta;
    @OneToOne
    private Autor autor;
    @OneToOne
    private Foto foto;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    

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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public boolean isVoto() {
        return voto;
    }

    public void setVoto(boolean voto) {
        this.voto = voto;
    }

    public boolean isAlta() {
        return alta;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Arte{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", anio=" + anio + ", precio=" + precio + ", voto=" + voto + ", alta=" + alta + ", autor=" + autor + ", foto=" + foto + ", categoria=" + categoria + '}';
    }

    

   
}

