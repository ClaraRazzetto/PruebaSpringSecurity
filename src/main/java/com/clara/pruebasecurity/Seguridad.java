package com.clara.pruebasecurity;

//en esta clase configuramos la seguridad de la web

import com.clara.pruebasecurity.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration //le avisa a Spring que debe usar esta clase para generar la configuración de la aplicación
@EnableWebSecurity //habilita a la clase para configurar la seguridad web
@EnableGlobalMethodSecurity(prePostEnabled = true) 
//permite autorizar las url, es decir, configurar los permisos
public class Seguridad extends WebSecurityConfigurerAdapter{
    
    //usamos el servicio para buscar usuarios por nombre de usuario
    @Autowired
    public ServicioUsuario servicioUsuario;
    
    //configuración del manejador de seguridad de SpringSecurity
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
        auth
        //especificamos el servicio que debe usar para autentificar un usuario
            .userDetailsService(servicioUsuario)
        //Setteamos un encriptador al servicio de usuario : cuando iniciamos sesión se compararan las contraseñas en base el encriptado utilizado.
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configuración de peticiones http: para configurar nuestro propio
    //inicio de sesión
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        
        http.headers().frameOptions().sameOrigin().and()
        //establecemos que los recursos que estan en las carpetas css,js,img estén permitidos para cualquier usuario aun cuando no este logeado
//Estan son las carpetas que estan dentro de la carpeta static
                .authorizeRequests()
                    .antMatchers("/css/")
                    .permitAll()
        //configuramos los permisos para el método de login
        //1. establecemos en que url se encuentra el formulario login 
        //el formulario login se retorna cuando se accede, dentro de nuestra plataforma, a la url "/login"
                .and().formLogin()
                    .loginPage("/login")
        /*2.Cual es la url que usa Spring Security debe usar para autentificar o procesar un usuario.
         En el formulario de inicio de sesión tenemos que poner el atributo action="/logincheck" */
                            .loginProcessingUrl("/logincheck")
        //3.Especificamos con que nombres viajan los valores de nombre usuario y contraseña en nuestra aplicación
                            .usernameParameter("username")
                            .passwordParameter("password")
        //4.Especificamos la url a la cual nos debe redirigir una vez que el usuario fue autenticado con exito
                            .defaultSuccessUrl("/inicio")
        //5.Especificamos la url a la cual nos redirige en caso de error en el login
                            .failureUrl("/login?error=error")
                            .permitAll()
                .and().logout()
        //Especificamos la url de cerrar sesión, cuando ingresamos a esta url Spring Security desvincula al usuario
        //En el botón de cerrar sesión tenemos que poner el atributo href="/logout"
                    .logoutUrl("/logout")
        //Especificamos la url a la que nos redirige Spring cuando cerramos sesion con exito
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
        //Opcional usarlo habilitado: te pide reingresar la contraseña para cambiar valores de la base de datos. Por ejemplo: para transferir plata en un banco
                .and().csrf().disable();
                
    }
}
