package com.vmtecnologia.test.userapi.service;


import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String email) {

        // Para simular/testar atomicidade se preciso
        if (email.contains("fail")) {
            throw new RuntimeException("Erro ao enviar email");
        }

        // Simulação de envio de email
        System.out.println("Email enviado para: " + email);
    }

}
