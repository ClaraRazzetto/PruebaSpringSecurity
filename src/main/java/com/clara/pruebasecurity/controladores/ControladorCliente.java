package com.clara.pruebasecurity.controladores;

import com.clara.pruebasecurity.entidades.Cliente;
import com.clara.pruebasecurity.excepciones.Excepcion;
import com.clara.pruebasecurity.servicios.ServicioCliente;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ControladorCliente {

    @Autowired
    private ServicioCliente servicioCliente;
    
     @GetMapping("/registro")
    public String registro(ModelMap model) {
        return "formCliente";
    }

    @PostMapping("/registrar")
    public String registrar(ModelMap model, @RequestParam String username, @RequestParam String password, @RequestParam String password2, @RequestParam String nombre, @RequestParam String apellido) {
        try {
            servicioCliente.registro(username, password, password2, nombre, apellido);
            return "redirect:/";
        } catch (Excepcion ex) {
            model.put("error", ex.getMessage());
            
            model.put("username", username);
            model.put("password", password);
            model.put("password2", password2);
            model.put("nombre", nombre);
            model.put("apellido", apellido);

            return "formCliente";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @GetMapping("/editar")
    public String editar(HttpSession session, ModelMap model, @RequestParam String id) {
        Cliente login = (Cliente) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
        try {
            model.addAttribute("perfil", servicioCliente.findById(id));
        } catch (Excepcion ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "editar-cliente";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE')")
    @PostMapping("/actualizar")
    public String actualizar(ModelMap model, HttpSession session, @RequestParam String id, @RequestParam String username, @RequestParam String password, @RequestParam String password2, @RequestParam String nombre, @RequestParam String apellido) {
        Cliente cliente = null;
        try {
            Cliente login = (Cliente) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            cliente = servicioCliente.findById(id);
            servicioCliente.actualizar(id, username, password, password2, nombre, apellido);
            session.setAttribute("usuariosession", cliente);
            return "redirect:/inicio";
        } catch (Excepcion ex) {
            model.put("error", ex.getMessage());
            model.put("perfil", cliente);
            return "editar-cliente";
        }
    }
}
