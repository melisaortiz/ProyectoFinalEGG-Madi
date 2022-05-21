package com.arte.madi.servicios;


import com.arte.madi.entidades.Foto;
import com.arte.madi.repositorios.FotoRepositorio;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase tiene la responsabilidad de llevar adelante las funcionalidades
 * necesarias para administrar fotos (creación y modificación).
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Service
public class FotoServicio {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    /**
     * Método para guardar la foto; "multipartfile" es la interfaz que modela el
     * archivo donde se almacena la foto.
     *
     * @param archivo
     * @return
     * @throws IOException
     */
    @Transactional
    public Foto guardar(MultipartFile archivo) throws IOException {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                // La lectura del contenido es la que puede generar un error, por eso está todo en un try/catch:
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Método para actualizar una foto.
     *
     * @param idFoto
     * @param archivo
     * @return
     * @throws IOException
     */
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws IOException {
        if (archivo != null) {
            try {
                Foto foto = new Foto();
                if (idFoto != null) {
                    foto = fotoRepositorio.getById(idFoto);
                }
                // Se actualizan los datos de la foto:
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                // La lectura del contenido es la que puede generar un error, por eso está todo en un try/catch:
                foto.setContenido(archivo.getBytes());
                return fotoRepositorio.save(foto);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

}
