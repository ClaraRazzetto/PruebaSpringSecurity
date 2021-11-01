package com.clara.pruebasecurity.servicios;

import com.clara.pruebasecurity.entidades.Persona;
import com.clara.pruebasecurity.entidades.Usuario;
import com.clara.pruebasecurity.repositorios.RepositorioUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
/*Para autentificar un usuario debemos implementar la interface UserDetailsService que establece Spring Security
Esta interface nos obliga a implementar el método abstracto loadUserByUsername */
public class ServicioUsuario implements UserDetailsService{

    @Autowired
    private RepositorioUsuario repositorioUsuario;
    
     /* Vamos a sobreescribir el método:
El método recibe el nombre de usuario de nuestro dominio ,lo busca en el repositorio, crear los permisos de ese usuario y lo transforma en un usuario del dominio de Spring Security
 Se llama a este método cuando la usuario quiere autentificarse (iniciar sesión) en la plataforma
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//instanciamos un objeto usuario de nuestro dominio, la buscamos por nombre de usuario
        Usuario usuario = repositorioUsuario.buscarPorUsername(username);
        //si no es null
        if (usuario != null) {
            /*creamos una la lista de permisos : los permisos se usan para permitir visualizar vistas o utilizar ciertas funcionalidades a los usuario según su rol en la app
        La clase GrantedAuthority es la que va a contener la lista de permisos */
            List<GrantedAuthority> permisos = new ArrayList<>();
            //creamos un permiso
            //este permiso me asigna el rol del usuario 
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol());
            //agregamos el permiso a la lista
            permisos.add(p1);

            /*En este punto ya tenemos autenticado al usuario, entonces, hacemos una llamada para guardar el usuario que traigo de la base de datos y meterlo en la sesion web, es decir, vamos a obtener sus datos para usarlos mientras este logeado.
             */
            //se recuperan los atributos del request de la solicitud HTTP
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            //traemos la solicitud con .getRequest() y se solicitan los datos de la session con .getSession
            HttpSession session = attr.getRequest().getSession(true);
            //en los datos de la session setteamos una veriable usuariosession y le guardamos el objeto usuario que buscamos en la base de datos
            session.setAttribute("usuariosession", usuario);

            /*EJ para mostrar datos en un vista:
                con el th:if verificamos esten setteados  en la session los datos de usuariosession y con el th:text mostramos el nombre y el apellido
                <h2 th:if="${session.usuariosession != null}" th:text="${session.usuariosession.nombre + ' ' + session.usuariosession.apellido}"></h2>
             */
            //Creamos un usuario de Spring, le pasamos al constructor el nombre de usuario, la contraseña y la lista de permisos, y lo retornamos
            return new User(usuario.getUsername(), usuario.getPassword(), permisos);
        } else {
            //si la usuario no existe devolvemos un usuario de Spring nulo
            return null;
        }
    }
}
