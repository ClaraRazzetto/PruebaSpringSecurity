package com.clara.pruebasecurity.controladores;

import com.clara.pruebasecurity.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class ControladorUsuario {
    
    @Autowired
    private ServicioUsuario servicioUsuario;

    @GetMapping("")
    //el error y el logout se envian como parámetros en la peticion http 
    public String login(ModelMap model, @RequestParam(required = false) String error, @RequestParam(required = false) String username, @RequestParam(required = false) String logout) {
        if (error != null) {
            //si se genera un error en el inicio de sesión se manda este mensaje
            model.put("error", "Nombre de usuario o clave incorrecto");
        }
        if (username != null) {
            //reenviamos el usuario para que no lo tenga que volver a completar  
            model.put("username", username);
        }
        if (logout != null) {
            //cuando cerramos sesión con exito mandamos un mensaje
            model.put("logout", "Has cerrado sesión correctamente");
        }
        return "login";
    }
}
