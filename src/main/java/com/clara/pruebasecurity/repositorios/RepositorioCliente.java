package com.clara.pruebasecurity.repositorios;

import com.clara.pruebasecurity.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioCliente extends JpaRepository<Cliente, String>{

    @Query("SELECT c FROM Cliente c WHERE c.username =:username")
    public Cliente buscarPorUsername(@Param("username") String username);
}
