package com.clara.pruebasecurity.servicios;


import com.clara.pruebasecurity.entidades.Foto;
import com.clara.pruebasecurity.excepciones.Excepcion;
import com.clara.pruebasecurity.repositorios.RepositorioFoto;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ServicioFoto {
    
    @Autowired
    private RepositorioFoto repositorioFoto;
    
    //MultipartFile archivo donde se almacena el archivo
    //de la foto
    @Transactional
    public Foto guardar(MultipartFile archivo) throws Excepcion{
        if (archivo != null) {
            try {
                 Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                // esta lectura del contenido en bytes
                //puede generar error (try-catch)
                foto.setContenido(archivo.getBytes());
                return repositorioFoto.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        //si el archivo falla creo uno en blanco
        return null; 
    }
    
    //ACTUALIZAR
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws Excepcion{
         if (archivo != null) {
            try {
                Foto foto = new Foto();
                //Si ya habia una foto, busco el id y la cambio
                if (idFoto != null) {
                    Optional<Foto> resp = repositorioFoto.findById(idFoto);
                    if (resp.isPresent()) {
                        foto = resp.get();
                    }
                }
                //si no tenia foto también funciona
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
               
                return repositorioFoto.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        //si el archivo falla creo uno en blanco
        return null; 
        
    }

}
