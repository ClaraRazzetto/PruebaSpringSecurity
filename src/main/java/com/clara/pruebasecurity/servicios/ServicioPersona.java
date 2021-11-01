package com.clara.pruebasecurity.servicios;

import com.clara.pruebasecurity.entidades.Foto;
import com.clara.pruebasecurity.excepciones.Excepcion;
import com.clara.pruebasecurity.repositorios.RepositorioPersona;
import com.clara.pruebasecurity.entidades.Persona;
import com.clara.pruebasecurity.enums.Roles;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioPersona{

    @Autowired
    private RepositorioPersona repositorio;

    @Autowired
    private ServicioFoto servicioFoto;

    //todos los métodos que generan modificaciones en la base de datos deben tener la notación @Transactional
    @Transactional
    public Persona registro(MultipartFile archivo, String username, String password, String password2, String dni, String nombre, String apellido) throws Excepcion {

        validar(username, password, password2, dni, nombre, apellido);

        if (buscarPorDni(dni) != null) {
            throw new Excepcion("Ya existe un persona con ese dni");
        }

        if (repositorio.buscarPorUsername(username) != null) {
            throw new Excepcion("Ya existe una persona con este nombre de persona");
        }

        Persona persona = new Persona();
        persona.setUsername(username);

        //Encriptamos la contraseña con el mismo encriptador que setteamos en la configuración de Spring Security
        persona.setPassword(new BCryptPasswordEncoder().encode(password));

        persona.setDni(dni);
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        //setteamos el rol
        persona.setRol(Roles.PERSONA);
        
        //guardamos la foto 
        Foto foto = servicioFoto.guardar(archivo);
        //se la setteamos a la persona
        persona.setFoto(foto);

        return repositorio.save(persona);
    }

    @Transactional
    public Persona actualizar(MultipartFile archivo, String id, String username, String password, String password2, String dni, String nombre, String apellido) throws Excepcion {

        validar(username, password, password2, dni, nombre, apellido);

        Persona persona = findById(id);

        persona.setUsername(username);
        persona.setPassword(new BCryptPasswordEncoder().encode(password));
        persona.setDni(dni);
        persona.setNombre(nombre);
        persona.setApellido(apellido);

        /* Si la foto existe se obtiene su id y se actualiza.
            De lo contrario se envia un idFoto null y en el método actualizar se crea un nuevo objeto foto */
        String idFoto = null;
        if (persona.getFoto() != null) {
            idFoto = persona.getFoto().getId();
        }
        Foto foto = servicioFoto.actualizar(idFoto, archivo);
        persona.setFoto(foto);

        return repositorio.save(persona);
    }

    public void validar(String username, String password, String password2, String dni, String nombre, String apellido) throws Excepcion {
        if (username == null || username.isEmpty()) {
            throw new Excepcion("El persona no puede ser nulo");
        }
        if (password == null || password2 == null || password.isEmpty() || password2.isEmpty()) {
            throw new Excepcion("La contraseña no puede ser nula");
        }
        if (password.length() > 10 || password2.length() > 10 || password.length() < 5 || password2.length() < 5) {
            throw new Excepcion("La contraseña debe tener un mínimo de 5 caracteres y un máximo de 10 caracteres");
        }
        if (!password.equals(password2)) {
            throw new Excepcion("Las contraseñas deben ser identicas");
        }
        if (dni == null || dni.isEmpty()) {
            throw new Excepcion("El DNI no puede ser nulo");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new Excepcion("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new Excepcion("El apellido no puede ser nulo");
        }
    }

    public List<Persona> listAll() {
        return repositorio.findAll();
    }

    public Persona findById(String id) throws Excepcion {
        Optional<Persona> opcional = repositorio.findById(id);
        if (opcional.isPresent()) {
            return opcional.get();
        } else {
            throw new Excepcion("No se encuentra el usuario solicitado");
        }
    }

    public Persona buscarPorDni(String dni) {
        return repositorio.BuscarPorDni(dni);
    }

}
