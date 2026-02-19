package com.vmtecnologia.test.userapi.service;

import org.springframework.stereotype.Service;

/**
 * Serviço responsável por simular o envio de e-mails.
 *
 * Neste projeto o envio não é realizado de forma real, pois o objetivo é apenas
 * representar a integração com um serviço externo (ex: SMTP, API de e-mail, etc).
 *
 * A simulação também permite testar comportamento transacional:
 * caso ocorra falha no envio, uma exceção é lançada para que o Spring realize
 * rollback da operação no banco de dados (garantindo atomicidade).
 */
@Service
public class EmailService {

    /**
     * Simula o envio de e-mail para o usuário.
     *
     * @param email endereço de destino.
     * @throws RuntimeException quando o e-mail contém "fail",
     *                          simulando erro de infraestrutura.
     */
    public void sendEmail(String email) {

        // Simula falha para validar rollback da transação
        if (email.contains("fail")) {
            throw new RuntimeException("Simulated e-mail sending failure.");
        }

        // Apenas simula o envio (não há integração real)
        System.out.println("=== Simulated E-mail Sending ===");
        System.out.println("To: " + email);
        System.out.println("Message: Your registration was completed successfully.");
        System.out.println("================================");
    }
}