package com.ashis.journalApp.service;

import com.ashis.journalApp.entity.JournalEntry;
import com.ashis.journalApp.entity.User;
import com.ashis.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JournalEntryService {

    @Autowired //DI
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String userName) {
            //find user
            User user = userService.findByUserName(userName);
            //save journal entry locally in saved
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            //added saved to user's journal Entry
            user.getJournalEntries().add(saved);
            userService.saveEntry(user); //lastly saved the user in db with new journal entry

    }

    public void saveEntry(JournalEntry journalEntry) {
       journalEntryRepository.save(journalEntry);

    }
    public List<JournalEntry> getAll()
    {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByObjId(ObjectId id)
    {
        return journalEntryRepository.findById(id);
    }
    public void deleteById(ObjectId id, String userName)
    {
        //find user
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id)); //remove that journal entry ref that we have deleted
        userService.saveEntry(user); //update user
        journalEntryRepository.deleteById(id);
    }
}
