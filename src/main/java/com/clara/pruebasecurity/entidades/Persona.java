package com.clara.pruebasecurity.entidades;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Persona extends Usuario{
    
    private String dni;
    private String nombre;
    private String apellido;
    @OneToOne
    private Foto foto;
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }
    
    
}
