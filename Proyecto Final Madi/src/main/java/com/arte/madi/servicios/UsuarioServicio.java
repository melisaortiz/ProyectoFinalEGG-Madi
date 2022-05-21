package com.arte.madi.servicios;


import com.arte.madi.entidades.Foto;
import com.arte.madi.entidades.Usuario;
import com.arte.madi.enums.Rol;
import com.arte.madi.repositorios.UsuarioRepositorio;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * Esta clase tiene la responsabilidad de llevar adelante las funcionalidades
 * necesarias para administrar usuarios (consulta, creación, modificación y dar
 * de baja).
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private FotoServicio fotoServicio;

  

    /**
     * Método de Registro de Usuario:
     *
     * @param archivo --> foto de perfil
     * @param nombre
     * @param apellido
     * @param dni
     * @param telefono
     * @param mail
     * @param clave
     * @param clave2 (para validar la clave)
     * @throws IOException
     * @throws Exception
     */
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String dni, String telefono, String mail, String clave, String clave2) throws IOException, Exception {
        // Antes de persistir, hay que asegurarse de que los datos obligatorios no lleguen vacíos (sean válidos).
        validar(nombre, apellido, dni, telefono, mail, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);
        usuario.setTelefono(telefono);
        usuario.setMail(mail);
        usuario.setAlta(new Date());
        //Seteo de forma automática el rol de USUARIO:
        usuario.setRol(Rol.USUARIO);
        // Encriptación de la clave:
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        // Seteo de la foto:
        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);
        // Persistencia en la DB:
        usuarioRepositorio.save(usuario);
    }

    /**
     * Método para Modificación de Usuario:
     *
     * @param id
     * @param archivo
     * @param nombre
     * @param apellido
     * @param dni
     * @param telefono
     * @param mail
     * @param clave
     * @param clave2
     * @throws Exception
     */
    @Transactional
    public void modificar(String id, MultipartFile archivo, String nombre, String apellido, String dni, String telefono, String mail, String clave, String clave2) throws Exception {
        // Antes de persistir, hay que asegurarse de que los datos obligatorios no lleguen vacíos (sean válidos).
        validar(nombre, apellido, dni, telefono, mail, clave, clave2);
        // Usamos el repositorio para que busque el usuario cuyo id sea el pasado como parámetro.
        Usuario usuario = usuarioRepositorio.getById(id);
        if (usuario != null) {
            // El usuario con ese id SI existe en la DB
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setMail(mail);
            usuario.setTelefono(telefono);
            // Encriptación de la clave:
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);
            // Seteo de la foto (en caso de haberla modificado):
            if (!archivo.isEmpty()) {
                String idFoto = null;
                if (usuario.getFoto() != null) {
                    idFoto = usuario.getFoto().getId();
                }
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                usuario.setFoto(foto);
            }
            // Persistencia en la DB:
            usuarioRepositorio.save(usuario);
        } else {
            // El usuario con ese id NO existe en la DB
            throw new Exception("No se encontró el usuario solicitado.");
        }
    }

    /**
     * El método borra el usuario de la DB (no se utiliza para darlo de baja).
     *
     * @param id
     * @throws Exception
     */
    @Transactional
    public void eliminar(String id) throws Exception {
        try {
            // Usamos el repositorio para que busque el usuario cuyo id sea el pasado como parámetro.
            Usuario usuario = usuarioRepositorio.getById(id);
            // Buscamos todos los préstamos del usuario para eliminarlos:
            
            if (usuario != null) {
                // Persistencia en la DB:
                usuarioRepositorio.delete(usuario);
            } else {
                throw new Exception("No existe el usuario vinculado a ese ID.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Método para Deshabilitar (dar de baja) el Usuario por ID:
     *
     * @param id
     * @throws Exception
     */
    @Transactional
    public void deshabilitar(String id) throws Exception {
        Usuario usuario = usuarioRepositorio.getById(id);
        if (usuario != null) {
            // El usuario con ese id SI existe en la DB
            usuario.setBaja(new Date());
            // El repositorio actualiza el objeto tipo usuario en la DB:
            usuarioRepositorio.save(usuario);
        } else {
            // El usuario con ese id NO existe en la DB
            throw new Exception("No se encontró el usuario solicitado.");
        }
    }

    /**
     * Método para Habilitar (borrar la baja) el Usuario por ID:
     *
     * @param id
     * @throws Exception
     */
    @Transactional
    public void habilitar(String id) throws Exception {
        Usuario usuario = usuarioRepositorio.getById(id);
        if (usuario != null) {
            // El usuario con ese id SI existe en la DB
            if (usuario.getBaja() == null) {
                throw new Exception("El usuario no se encuentra dado de baja.");
            } else {
                usuario.setBaja(null); // Le borramos la fecha de baja!!
                // El repositorio actualiza el objeto tipo usuario en la DB:
                usuarioRepositorio.save(usuario);
            }
        } else {
            // El usuario con ese id NO existe en la DB
            throw new Exception("No se encontró el usuario solicitado.");
        }
    }

    /**
     * Método para cambiar el rol de un usuario por ID.
     *
     * @param id
     */
    @Transactional
    public void cambiarRol(String id) {
        Usuario usuario = usuarioRepositorio.getById(id);
        if (usuario != null) {
            // El usuario con ese id SI existe en la DB
            if (usuario.getRol().equals(Rol.USUARIO)) { // Se usa el equals pero Rol.USUARIO no va entre comillas, no es un String, es el String de un enum.
                usuario.setRol(Rol.ADMIN);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USUARIO);
            }
        }
    }

    /**
     * Método para validar nombre, apellido, dni, mail y clave de usuario,
     * creado para no repetir la lógica en otros métodos:
     *
     * @param nombre
     * @param apellido
     * @param dni
     * @param telefono
     * @param mail
     * @param clave
     * @param clave2
     * @throws Exception
     */
    public void validar(String nombre, String apellido, String dni, String telefono, String mail, String clave, String clave2) throws Exception {
        if (nombre == null || nombre.isEmpty()) {
            throw new Exception("El nombre del usuario es obligatorio.");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new Exception("El apellido del usuario es obligatorio.");
        }
        if (dni == null || dni.isEmpty()) {
            throw new Exception("El DNI del usuario es obligatorio.");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new Exception("El telefono del usuario es obligatorio.");
        }
        if (mail == null || mail.isEmpty()) {
            throw new Exception("El mail del usuario es obligatorio.");
        }
        if (clave == null || clave.isEmpty() || clave.length() < 6) {
            throw new Exception("La clave del usuario es obligatoria y debe tener al menos 6 caracteres.");
        }
        if (!clave2.equals(clave)) {
            throw new Exception("Las claves ingresadas deben coincidir.");
        }
    }

    /**
     * Método que se llamará cuando el usuario quiere atentificarse en la
     * plataforma
     *
     * @param mail
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            // Incorporo a la lista de permisos el de USUARIO:
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());
            permisos.add(p1);

            // Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado:
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            // Traigo mail y clave del usuario para iniciar sesión; también la lista de permisos
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

// ------------------------------ MÉTODOS DEL REPOSITORIO ------------------------------
    /**
     * Busca un usuario por id.
     *
     * @param id
     * @return
     */
    public Usuario getById(String id) {
        return usuarioRepositorio.getById(id);
    }

    /**
     * Devuelve todos los usuarios registrados en la DB.
     *
     * @return
     */
    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    /**
     * Devuelve todos los usuarios registrados con fecha de baja nula.
     *
     * @return
     */
    public List<Usuario> buscarActivos() {
        return usuarioRepositorio.buscarActivos();
    }

    /**
     * Devuelve todos los usuarios registrados con fecha de baja NO nula.
     *
     * @return
     */
    public List<Usuario> buscarInactivos() {
        return usuarioRepositorio.buscarInactivos();
    }
}
