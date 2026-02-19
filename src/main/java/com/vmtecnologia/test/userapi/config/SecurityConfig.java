package com.vmtecnologia.test.userapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuração responsável por definir as regras de segurança da aplicação.
 *
 * Nesta etapa do projeto foi utilizada uma configuração simples com autenticação
 * HTTP Basic e usuario em memória, suficiente para proteger os endpoints durante
 * testes e validação do funcionamento da API.
 *
 * Não há integração com banco de dados ou JWT, pois o objetivo aqui é apenas
 * demonstrar controle de acesso básico.
 */
@Configuration
public class SecurityConfig {

    /**
     * Define a cadeia de filtros de segurança utilizada pelo Spring Security.
     *
     * Configurações aplicadas:
     * - CSRF desabilitado
     * - Todas as requisições exigem autenticação.
     * - Autenticação via HTTP Basic (usuario e senha enviados no header).
     *
     * HTTP Basic foi escolhido por ser simples e suficiente para testes locais.
     *
     * @param http objeto de configuração do Spring Security.
     * @return SecurityFilterChain configurado.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // APIs REST normalmente não utilizam CSRF pois não trabalham com sessão
                .csrf(csrf -> csrf.disable())

                // Qualquer requisição precisa estar autenticada
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )

                // Define o tipo de autenticação como HTTP Basic
                // (usuário e senha enviados via Authorization Header)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Define um usuario em memória para autenticação.
     *
     * Essa abordagem é usada apenas para desenvolvimento e testes,
     * evitando a necessidade de criar tabela de usuarios ou integração
     * com banco de dados neste momento.
     *
     * Usuario criado:
     * - username: admin
     * - password: admin123
     * - role: ADMIN
     *
     * {noop} indica que a senha não está criptografada (sem encoder),
     * adequado apenas para ambiente de desenvolvimento.
     *
     * @return UserDetailsService com usuario configurado em memória.
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withUsername("admin")
                .password("{noop}admin123") // {noop} = sem criptografia (apenas para testes)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}