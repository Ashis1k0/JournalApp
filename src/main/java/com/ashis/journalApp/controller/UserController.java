package com.ashis.journalApp.controller;

import com.ashis.journalApp.entity.User;
import com.ashis.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // GET /user
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    // POST /user
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveEntry(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create user: " + e.getMessage());
        }
    }

    // PUT /user/{userName}
    @PutMapping("{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName) {
        User userInDb = userService.findByUserName(userName);
        if (userInDb == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        // Update only non-empty fields
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            userInDb.setUserName(user.getUserName());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userInDb.setPassword(user.getPassword());
        }
        userService.saveEntry(userInDb);
        return ResponseEntity.noContent().build();
    }


}
