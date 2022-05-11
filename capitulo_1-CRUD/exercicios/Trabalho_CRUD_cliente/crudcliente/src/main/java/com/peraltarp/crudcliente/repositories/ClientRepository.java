package com.peraltarp.crudcliente.repositories;

import com.peraltarp.crudcliente.entities.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// acessar os dados no banco de dados 
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{
    
}
