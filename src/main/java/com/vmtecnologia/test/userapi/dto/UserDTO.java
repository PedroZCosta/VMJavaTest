package com.vmtecnologia.test.userapi.dto;

/**
 * Data Transfer Object (DTO) utilizado para expor dados do usuário nas respostas da API.
 *
 * O DTO é usado para desacoplar a entidade de persistência (User) da camada de apresentação,
 * evitando expor informações sensíveis, como a senha, e permitindo controlar exatamente
 * quais campos serão retornados ao cliente.
 *
 * Neste caso, apenas id, name e email são disponibilizados.
 */
public record UserDTO(
        Long id,
        String name,
        String email
) {}