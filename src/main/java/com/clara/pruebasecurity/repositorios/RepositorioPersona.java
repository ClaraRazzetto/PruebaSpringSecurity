package com.clara.pruebasecurity.repositorios;

import com.clara.pruebasecurity.entidades.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioPersona extends JpaRepository<Persona, String>{

    @Query("SELECT p FROM Persona p WHERE p.dni =:dni")
    public Persona BuscarPorDni(@Param("dni") String dni);
    
    @Query("SELECT p FROM Persona p WHERE p.username =:username")
    public Persona buscarPorUsername(@Param("username") String username);
    
}
