package com.example.ecommerce.service;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User create(User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        if (u.getRole() == null) u.setRole("ROLE_USER");
        return repo.save(u);
    }

    public User getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<User> getAll() { return repo.findAll(); }

    public User update(Long id, User u) {
        User ex = getById(id);
        ex.setName(u.getName());
        ex.setEmail(u.getEmail());
        if (u.getPassword() != null && !u.getPassword().isBlank()) ex.setPassword(encoder.encode(u.getPassword()));
        ex.setRole(u.getRole());
        return repo.save(ex);
    }

    public void delete(Long id) {
        User u = getById(id);
        repo.delete(u);
    }
}
