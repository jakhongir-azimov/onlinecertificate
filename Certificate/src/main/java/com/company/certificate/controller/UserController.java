package com.company.certificate.controller;

import com.company.certificate.model.User;
import com.company.certificate.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        boolean result = userService.create(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<User> result = userService.getAll();
        return ResponseEntity.ok(result);
    }
}
