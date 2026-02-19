package com.vmtecnologia.test.userapi.service;

import com.vmtecnologia.test.userapi.model.User;
import com.vmtecnologia.test.userapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);

        // requisito do teste: enviar email após salvar
        emailService.sendEmail(savedUser.getEmail());

        return savedUser;
    }

    public Page<User> listUsers(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return userRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}
