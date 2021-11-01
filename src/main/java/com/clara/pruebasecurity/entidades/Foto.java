package com.clara.pruebasecurity.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
 
    //El atributo tipo mime asigna el formato del archivo de la foto
    // especificaciones para la cabecera de los mensajes que enviamos al navegador
    private String mime;
    private String nombre;
    
    //Lob identifica que el tipo de dato es pesado: 
    //la BD usa un tipo especifico para almacenar el contenido
    //Basic(..) indica que cargue el contenido sólo cuando 
    //lo pidamos haciendo que los querys sean más livianos
    //cuando consulto por la foto sólo trae los atributos
    //que están marcados como viber(los comunes) y mediante un get
    //traigo los atributos lazy 
    @Lob @Basic(fetch = FetchType.LAZY)
    //mandamos el contenido en un arreglo de bytes
    private byte[] contenido; 

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
}
