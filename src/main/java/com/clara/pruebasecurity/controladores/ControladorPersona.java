package com.clara.pruebasecurity.controladores;

import com.clara.pruebasecurity.entidades.Persona;
import com.clara.pruebasecurity.excepciones.Excepcion;
import com.clara.pruebasecurity.servicios.ServicioPersona;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/persona")
public class ControladorPersona {
    
    @Autowired
    private ServicioPersona servicio;

    @GetMapping("/registro")
    public String registro(ModelMap model) {
        return "formPersona";
    }

    @PostMapping("/registrar")
    /*El fomulario de la vista debe tener en su etiqueta los argumentos :
        * method="POST" : indicamos que es una petición HTTP POST (guardamos información)
        * action="/registro/save" : indicamos la url a la que enviamos los datos al presionar el botón submit

    Con @RequestParam vinculamos los parámetros de una solicitud http a los argumentos del método
    En las etiquetas input usamos el atributo name="nombreParámetro" : para identificar o vincular cada parámetro con el argumento del método
    que le corresponda.
    (Tienen que tener el mismo nombre)

    Cuando recibimos una foto como parámetro usamos MultipartFile en los argumentos del método
       En la vista debemos usar los atributos : enctype="multipart/form-data" dentro de la etiqueta del formulario y en la etiqueta input
        donde cargamos la foto type="file"
    
    <form method="POST" action="/registro/save" enctype="multipart/form-data">
        <input type="text" name="nombre">
        <input type="file" name="archivo">
    </form>
    */
    public String registrar(ModelMap model,MultipartFile archivo, @RequestParam String username, @RequestParam String password, @RequestParam String password2, @RequestParam String dni, @RequestParam String nombre, @RequestParam String apellido) {
        try {
            //Recibimos los parámetros y los enviamos al método registro para validarlos y guardarlos en nuestro objeto Persona
            servicio.registro(archivo,username, password, password2, dni, nombre, apellido);
            //si esto se realiza con exito, nos redirigimos al método con la url / que nos va a mostrar la vista de inicio
            return "redirect:/";
        } catch (Excepcion ex) {
            /* si se genera un error al validar los datos en el servicio, enviamos el error a la vista
            Usando ModelMap.put() o ModelMap.AddAtributes()
            En la vista mostramos el error usando los atributos th:if (se muestra si el valor es true o no se muestra si es false) y th:text para mostrar el mensaje de manera dinámica (pueden ser mensajes distintos)
            <p th:if="${error}" th:text="${error}"></p>
            */
            model.put("error", ex.getMessage());

            /*Usando modelMap enviamos al formulario los valores para poder usarlos nuevamente y no tener que completar todos los campos en blanco
            En la etiqueta correspondiente usamos el atributo th:value="${nombreAtributo} para enlazarlo
            */
            model.put("username", username);
            model.put("password", password);
            model.put("password2", password2);
            model.put("dni", dni);
            model.put("nombre", nombre);
            model.put("apellido", apellido);

            //en este caso como hubo error, devolvemos la vista form para volver a completar el formulario 
            return "formPersona";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_PERSONA')")
    @GetMapping("/list")
    //usamos modelmap para enviar datos a la vista 
    public String list(ModelMap model) {
        //enviamos una lista de personas
        model.put("personas", servicio.listAll());
        return "list.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PERSONA')")
    @GetMapping("/editar")
    public String editar(HttpSession session,ModelMap model, @RequestParam String id) {
        //valido que la persona este logeada y que posea el mismo id que se el id de la session de usuario para que pueda modificar sus datos
        Persona login = (Persona) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id) ) {
            return "redirect:/inicio";
        }
        try {
            model.addAttribute("perfil", servicio.findById(id));  
        } catch (Excepcion ex) {
           model.addAttribute("error", ex.getMessage());
        }
        return "editar-persona";
    }

    @PreAuthorize("hasAnyRole('ROLE_PERSONA')")
    @PostMapping("/actualizar")
    public String actualizar(ModelMap model, HttpSession session, MultipartFile archivo,@RequestParam String id, @RequestParam String username, @RequestParam String password, @RequestParam String password2, @RequestParam String dni, @RequestParam String nombre, @RequestParam String apellido) {
        Persona persona = null;
        try {
            Persona login = (Persona) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id) ) {
                return "redirect:/inicio";
            }
            persona = servicio.findById(id);
            servicio.actualizar(archivo,id, username, password, password2, dni, nombre, apellido);
//            modificamos los datos de la sesion del usuario
            session.setAttribute("usuariosession", persona);
            return "redirect:/inicio";
        } catch (Excepcion ex) {
            model.put("error", ex.getMessage());
            model.put("perfil", persona); 
            return "editar-persona";
        }
    }
}
