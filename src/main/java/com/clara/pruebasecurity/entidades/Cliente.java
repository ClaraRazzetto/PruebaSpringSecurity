package com.clara.pruebasecurity.entidades;

import com.clara.pruebasecurity.enums.Roles;
import javax.persistence.Entity;

@Entity
public class Cliente extends Usuario{

    private String nombre;
    private String apellido;

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

}
