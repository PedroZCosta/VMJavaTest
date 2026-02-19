package com.vmtecnologia.test.userapi.controller;


import com.vmtecnologia.test.userapi.model.User;
import com.vmtecnologia.test.userapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public Page<User> listUsers(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return userService.listUsers(name, pageable);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

}
