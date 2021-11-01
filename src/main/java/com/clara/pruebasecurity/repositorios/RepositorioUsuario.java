package com.clara.pruebasecurity.repositorios;

import com.clara.pruebasecurity.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, String>{

    @Query("SELECT u FROM Usuario u WHERE u.username =:username")
    public Usuario buscarPorUsername(@Param("username") String username);
}
