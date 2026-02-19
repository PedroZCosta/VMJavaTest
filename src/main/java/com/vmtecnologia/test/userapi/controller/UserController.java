package com.vmtecnologia.test.userapi.controller;

import com.vmtecnologia.test.userapi.dto.UserDTO;
import com.vmtecnologia.test.userapi.model.User;
import com.vmtecnologia.test.userapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável por gerenciar os usuarios.
 *
 * Esta classe expõe os endpoints HTTP da aplicação e delega as regras de negócio
 * para a camada de serviço (UserService), mantendo a separação de responsabilidades.
 *
 * Funcionalidades disponíveis:
 * - Cadastro de usuarios
 * - Consulta por ID
 * - Listagem com filtro por nome e paginação
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Injeção de dependência via construtor.
     * O Spring gerencia automaticamente a instância de UserService.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para criação de um novo usuario.
     *
     * Metodo HTTP: POST
     *
     * O usuario é validado com Bean Validation (@Valid) antes de ser enviado
     * para a camada de serviço. Após a persistência, é retornado um DTO
     * sem o campo de senha, garantindo que dados sensíveis não sejam expostos.
     *
     * @param user Dados do usuario a ser criado.
     * @return UserDTO contendo os dados persistidos (sem senha).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    /**
     * Endpoint para listagem de usuarios com suporte a filtro por nome e paginação.
     *
     * Metodo HTTP: GET
     *
     * O parâmetro "name" é opcional. Quando informado, realiza busca parcial
     * (case insensitive). Caso contrário, retorna todos os usuarios.
     *
     * Pageable é resolvido automaticamente pelo Spring através dos parâmetros:
     * ?page=0&size=10&sort=name,asc
     *
     * @param name Nome (ou parte do nome) para filtro opcional.
     * @param pageable Informações de paginação fornecidas pela requisição.
     * @return Página contendo UserDTO.
     */
    @GetMapping
    public Page<UserDTO> listUsers(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return userService.listUsers(name, pageable);
    }

    /**
     * Endpoint para buscar um usuario específico pelo ID.
     *
     * Metodo HTTP: GET
     *
     * Caso o usuario não seja encontrado, a exceção é tratada automaticamente
     * pelo Spring e retornará HTTP 404.
     *
     * @param id Identificador do usuario.
     * @return UserDTO correspondente ao usuário encontrado.
     */
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }
}