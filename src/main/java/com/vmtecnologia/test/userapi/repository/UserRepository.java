package com.vmtecnologia.test.userapi.repository;

import com.vmtecnologia.test.userapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório responsável pelo acesso aos dados de usuários.
 *
 * Estende JpaRepository, o que fornece automaticamente operações CRUD
 * como salvar, buscar, deletar e listar sem necessidade de implementação manual.
 *
 * O Spring Data JPA gera as consultas dinamicamente a partir do nome dos métodos.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca usuários cujo nome contenha o valor informado,
     * ignorando diferenças entre maiúsculas e minúsculas,
     * com suporte à paginação.
     *
     * O Spring gera automaticamente o SQL baseado no nome do metodo.
     *
     * Exemplo gerado:
     * SELECT * FROM users WHERE LOWER(name) LIKE LOWER('%valor%')
     */
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}