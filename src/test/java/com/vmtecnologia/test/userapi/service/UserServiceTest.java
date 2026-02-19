package com.vmtecnologia.test.userapi.service;

import com.vmtecnologia.test.userapi.dto.UserDTO;
import com.vmtecnologia.test.userapi.model.User;
import com.vmtecnologia.test.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * CLASSE DE TESTE UNITÁRIO PARA UserService
 *
 * Objetivo:
 * Validar o comportamento da camada de serviço de forma isolada,
 * simulando as dependências (Repository e EmailService) com Mockito.
 *
 * Dessa forma, testamos apenas a regra de negócio sem precisar:
 * - conectar em banco de dados
 * - enviar e-mail real
 * - subir contexto completo do Spring
 */
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    /**
     * Mock do repositório.
     * Simula o acesso ao banco de dados.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * Mock do serviço de email.
     * Permite validar se o envio foi chamado sem enviar nada de verdade.
     */
    @Mock
    private EmailService emailService;

    /**
     * Classe que será testada.
     * O Mockito injeta automaticamente os mocks acima.
     */
    @InjectMocks
    private UserService userService;

    /**
     * TESTE 1 — Criar usuario com sucesso
     *
     * Verifica:
     * - Se o usuario é salvo no repositório
     * - Se o email é enviado após persistência
     * - Se o retorno é convertido corretamente para DTO
     *
     * Simulamos o comportamento do JPA retornando o usuario já com ID.
     */
    @Test
    void shouldCreateUserAndSendEmail() {

        User userToSave = new User();
        userToSave.setName("Pedro");
        userToSave.setEmail("pedro@email.com");
        userToSave.setPassword("123");

        // entidade como o JPA retornaria após salvar
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Pedro");
        savedUser.setEmail("pedro@email.com");
        savedUser.setPassword("123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(userToSave);

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail("pedro@email.com");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Pedro");
        assertThat(result.email()).isEqualTo("pedro@email.com");
    }

    /**
     * TESTE 2 — Filtrar usuarios por nome
     *
     * Verifica:
     * - Se o filtro chama o metodo correto do repository
     * - Se a conversão entity para DTO ocorre corretamente
     * - Se a paginação é respeitada
     */
    @Test
    void shouldFilterUsersByName() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setId(1L);
        user.setName("Pedro");
        user.setEmail("pedro@email.com");

        Page<User> pageFromRepo = new PageImpl<>(java.util.List.of(user));

        when(userRepository.findByNameContainingIgnoreCase("pedro", pageable))
                .thenReturn(pageFromRepo);

        Page<UserDTO> result = userService.listUsers("pedro", pageable);

        verify(userRepository).findByNameContainingIgnoreCase("pedro", pageable);

        assertThat(result.getContent()).hasSize(1);

        UserDTO dto = result.getContent().get(0);
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Pedro");
        assertThat(dto.email()).isEqualTo("pedro@email.com");
    }

    /**
     * TESTE 3 — Listar todos quando não há filtro
     *
     * Caso o parâmetro name seja null, o sistema deve:
     * - Buscar todos os usuarios
     * - Ainda assim converter corretamente para DTO
     */
    @Test
    void shouldListAllUsersWhenNameIsNull() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setId(1L);
        user.setName("Pedro");
        user.setEmail("pedro@email.com");

        Page<User> pageFromRepo = new PageImpl<>(java.util.List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(pageFromRepo);

        Page<UserDTO> result = userService.listUsers(null, pageable);

        verify(userRepository).findAll(pageable);

        assertThat(result.getContent()).hasSize(1);

        UserDTO dto = result.getContent().get(0);
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Pedro");
        assertThat(dto.email()).isEqualTo("pedro@email.com");
    }

    /**
     * TESTE 4 — Usuario não encontrado
     *
     * Verifica se o serviço retorna erro HTTP adequado (404)
     * quando o ID pesquisado não existe.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(org.springframework.web.server.ResponseStatusException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(99L);
    }

    /**
     * TESTE 5 — Garantir atomicidade da operação
     *
     * Simula falha no envio de email.
     * O objetivo é validar que a exceção é propagada,
     * permitindo rollback da transação (@Transactional).
     *
     * Isso garante consistência: não faz sentido salvar usuario
     * se o processo completo falhou.
     */
    @Test
    void shouldRollbackWhenEmailFails() {
        User user = new User();
        user.setName("Pedro");
        user.setEmail("fail@email.com"); // força erro
        user.setPassword("123");

        when(userRepository.save(any(User.class))).thenReturn(user);
        doThrow(new RuntimeException("Simulated e-mail sending failure"))
                .when(emailService).sendEmail(user.getEmail());

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository).save(user);
        verify(emailService).sendEmail(user.getEmail());
    }
}