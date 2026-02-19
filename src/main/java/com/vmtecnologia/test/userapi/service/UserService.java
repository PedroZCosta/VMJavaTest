package com.vmtecnologia.test.userapi.service;

import com.vmtecnologia.test.userapi.dto.UserDTO;
import com.vmtecnologia.test.userapi.model.User;
import com.vmtecnologia.test.userapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Serviço responsável pelas regras de negócio relacionadas a usuários.
 *
 * Aqui ficam concentradas as operações principais da aplicação:
 * criação, consulta e transformação dos dados antes de expor para a API.
 *
 * A camada de serviço faz a mediação entre Controller e Repository,
 * garantindo organização e separação de responsabilidades.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * A anotação @Transactional garante atomicidade:
     * se o envio do e-mail falhar, o usuário não será persistido no banco.
     *
     * @param user entidade recebida da requisição
     * @return UserDTO contendo apenas os dados públicos do usuário
     */
    @Transactional
    public UserDTO createUser(User user) {

        user.setName(user.getName().trim());
        user.setEmail(user.getEmail().trim());

        User savedUser = userRepository.save(user);

        // requisito do teste: envio de e-mail após persistência
        emailService.sendEmail(savedUser.getEmail());

        return toDTO(savedUser);
    }

    /**
     * Lista usuários com suporte a paginação e filtro por nome.
     *
     * Caso o parâmetro "name" seja informado, realiza busca parcial (LIKE).
     * Caso contrário, retorna todos os usuários paginados.
     *
     * O retorno é convertido para DTO para não expor dados sensíveis
     * como a senha armazenada na entidade.
     *
     * @param name filtro opcional
     * @param pageable controle de paginação fornecido pelo Spring
     * @return página de UserDTO
     */
    public Page<UserDTO> listUsers(String name, Pageable pageable) {

        Page<User> users;

        if (name != null && !name.isBlank()) {
            users = userRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        // converte cada entidade User em UserDTO
        return users.map(this::toDTO);
    }

    /**
     * Busca um usuário pelo ID.
     *
     * Se não encontrado, retorna erro 404 (NOT FOUND),
     * comportamento esperado em APIs REST.
     *
     * @param id identificador do usuário
     * @return UserDTO correspondente
     */
    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return toDTO(user);
    }

    /**
     * Metodo auxiliar responsável por converter a entidade User em UserDTO.
     *
     * Centralizar essa conversão evita repetição de código e mantém
     * a separação entre modelo de persistência e modelo de resposta da API.
     */
    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}