package com.ashis.journalApp.service;

import com.ashis.journalApp.entity.JournalEntry;
import com.ashis.journalApp.entity.User;
import com.ashis.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired //DI
    private UserRepository userRepository;

    public void saveEntry(User user) {
            userRepository.save(user);
    }
    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> findByObjId(ObjectId id)
    {
        return userRepository.findById(id);
    }
    public void deleteById(ObjectId id)
    {
        userRepository.deleteById(id);
    }
    public User findByUserName(String userName)
    {
        return userRepository.findByUserName(userName);
    }
}
