package com.clara.pruebasecurity.repositorios;

import com.clara.pruebasecurity.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioFoto extends JpaRepository<Foto, String>{

}
