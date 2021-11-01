package com.clara.pruebasecurity.servicios;

import com.clara.pruebasecurity.entidades.Cliente;
import com.clara.pruebasecurity.enums.Roles;
import com.clara.pruebasecurity.excepciones.Excepcion;
import com.clara.pruebasecurity.repositorios.RepositorioCliente;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServicioCliente{

    @Autowired 
    private RepositorioCliente repositorioCliente;
    
    
    //todos los métodos que generan modificaciones en la base de datos deben tener la notación @Transactional
    @Transactional
    public Cliente registro(String username, String password, String password2, String nombre, String apellido) throws Excepcion {

        validar(username, password, password2, nombre, apellido);

        if (repositorioCliente.buscarPorUsername(username) != null) {
            throw new Excepcion("Ya existe una cliente con este nombre de cliente");
        }

        Cliente cliente = new Cliente();
        cliente.setUsername(username);

        //Encriptamos la contraseña con el mismo encriptador que setteamos en la configuración de Spring Security
        cliente.setPassword(new BCryptPasswordEncoder().encode(password));

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        //setteamos el rol
        cliente.setRol(Roles.CLIENTE);
        
        return repositorioCliente.save(cliente);
    }

    @Transactional
    public Cliente actualizar(String id, String username, String password, String password2, String nombre, String apellido) throws Excepcion {

        validar(username, password, password2, nombre, apellido);

        Cliente cliente = findById(id);

        cliente.setUsername(username);
        cliente.setPassword(new BCryptPasswordEncoder().encode(password));
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);

        return repositorioCliente.save(cliente);
    }

    public void validar(String username, String password, String password2, String nombre, String apellido) throws Excepcion {
        if (username == null || username.isEmpty()) {
            throw new Excepcion("El cliente no puede ser nulo");
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
        if (nombre == null || nombre.isEmpty()) {
            throw new Excepcion("El nombre no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new Excepcion("El apellido no puede ser nulo");
        }
    }
    
    public Cliente findById(String id) throws Excepcion {
        Optional<Cliente> opcional = repositorioCliente.findById(id);
        if (opcional.isPresent()) {
            return opcional.get();
        }else{
            throw new Excepcion("No se encuentra el usuario solicitado");
        }
    }
}
