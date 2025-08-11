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

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    /**
     * Save a new entry and attach it to user. Returns saved JournalEntry.
     * Throws IllegalArgumentException if user not found.
     */
    public JournalEntry saveEntry(JournalEntry journalEntry, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userName);
        }

        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
        return saved;
    }

    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByObjId(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    /**
     * Delete entry and remove ref from user if entry existed.
     * Returns true if the entry existed and was deleted, false otherwise.
     */
    public boolean deleteById(ObjectId id, String userName) {
        Optional<JournalEntry> maybe = journalEntryRepository.findById(id);
        if (!maybe.isPresent()) {
            return false;
        }

        User user = userService.findByUserName(userName);
        if (user != null) {
            user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            userService.saveEntry(user);
        }
        journalEntryRepository.deleteById(id);
        return true;
    }

    public JournalEntry updateJournalById(ObjectId myId, JournalEntry updatedEntry) {
        JournalEntry oldEntry = journalEntryRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("No journal entry found with id: " + myId));

        oldEntry.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().equals("") ?
                updatedEntry.getTitle() : oldEntry.getTitle());
        oldEntry.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().equals("") ?
                updatedEntry.getContent() : oldEntry.getContent());

        return journalEntryRepository.save(oldEntry);
    }
}
