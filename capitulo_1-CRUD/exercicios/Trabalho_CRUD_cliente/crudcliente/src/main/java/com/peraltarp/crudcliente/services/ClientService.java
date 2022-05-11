package com.peraltarp.crudcliente.services;

// import java.util.ArrayList;
// import java.util.List;
import java.util.Optional;
// import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.peraltarp.crudcliente.dto.ClientDTO;
import com.peraltarp.crudcliente.entities.Client;
import com.peraltarp.crudcliente.repositories.ClientRepository;
import com.peraltarp.crudcliente.services.exceptions.DatabaseException;
import com.peraltarp.crudcliente.services.exceptions.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
// registra a classe como componente de dependencia do Client Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true) //melhora a performaace do banco de dados
    public Page<ClientDTO> findAllPaged(PageRequest pagerequest) {
        Page<Client> list = repository.findAll(pagerequest);        
        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        
        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada")); //lanca exceção
        return new ClientDTO(entity);
    }

    @Transactional(readOnly = true)
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity); 
    }

   
    @Transactional
    public ClientDTO update(Long id,ClientDTO dto) {
        // instanciar um obj do tipo Client
        try{
            Client entity = repository.getById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        }
        catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        }

    }
    
    public void delete(Long id) {
        try{
            repository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id not found: " + id);
        }
        catch(DataIntegrityViolationException e){
            throw new DatabaseException("Violação de Integridade");
        }
    }  


    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    
    }

    
    
}
