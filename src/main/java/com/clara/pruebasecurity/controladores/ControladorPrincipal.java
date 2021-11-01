package com.clara.pruebasecurity.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ControladorPrincipal{
        
    //Mostramos la vista index.html
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    /* @PreAuthorize("hasAnyRole('NOMBRE_ROL')")
    Usamos esta notación para establecer que Spring Security debe generar una autorización para ingresar a esta url y la regla que se tiene que cumplir para esto es que tenga alguno de los roles que pasamos como parametro
    Estos roles se encuentran en la lista de permisos que generamos para el usuario en el método loadUserByUsername que sobreescrimos en el servicio */
    
    //mostramos la vista inicio.html que solo puede ver la persona si esta registrada como usuario
    @PreAuthorize("hasAnyRole('ROLE_PERSONA', 'ROLE_CLIENTE')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }
    
}
