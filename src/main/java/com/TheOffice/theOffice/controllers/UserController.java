package com.TheOffice.theOffice.controllers;

import com.TheOffice.theOffice.daos.UserDao;
import com.TheOffice.theOffice.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userDao.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userDao.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String role = (String) request.get("role");
        BigDecimal wallet = new BigDecimal(request.get("wallet").toString());

        int userId = userDao.save(email, username, password, role, wallet);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", userId,
                "email", email,
                "username", username,
                "role", role,
                "wallet", wallet
        ));
    }



    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userDao.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        if(userDao.delete(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
