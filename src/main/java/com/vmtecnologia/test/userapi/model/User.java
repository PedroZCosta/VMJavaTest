package com.vmtecnologia.test.userapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Entidade que representa o usuário no banco de dados.
 *
 * Esta classe é mapeada com JPA/Hibernate para a tabela "users",
 * sendo responsável apenas pela persistência dos dados.
 *
 * As validações anotadas (@NotBlank, @Email) garantem integridade
 * dos dados antes de serem persistidos.
 *
 * A senha é armazenada nesta entidade, mas nunca deve ser exposta
 * diretamente nas respostas da API, por isso utilizei DTOs.
 */
@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    /**
     * E-mail do usuário.
     * Deve possuir formato válido e não pode ser vazio.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * Senha do usuário armazenada para autenticação.
     * Não é exposta na API, sendo omitida através do uso de DTO.
     */
    @NotBlank
    private String password;
}