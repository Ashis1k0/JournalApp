package com.ashis.journalApp.controller;

import com.ashis.journalApp.entity.JournalEntry;
import com.ashis.journalApp.entity.User;
import com.ashis.journalApp.service.JournalEntryService;
import com.ashis.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // GET /journal/{userName}
    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<JournalEntry> entries = user.getJournalEntries();
        if (entries != null && !entries.isEmpty()) {
            return ResponseEntity.ok(entries);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No entries found for user");
        }
    }

    // POST /journal/{userName}
    @PostMapping("{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            User user = userService.findByUserName(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            JournalEntry saved = journalEntryService.saveEntry(myEntry, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create entry");
        }
    }

    // GET /journal/id/{myId}
    @GetMapping("id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable String myId) {
        ObjectId oid;
        try {
            oid = new ObjectId(myId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid id format");
        }

        Optional<JournalEntry> journalEntry = journalEntryService.findByObjId(oid);
        if (journalEntry.isPresent()) {
            return ResponseEntity.ok(journalEntry.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
        }
    }

    // DELETE /journal/id/{userName}/{myId}
    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable String userName, @PathVariable String myId) {
        ObjectId oid;
        try {
            oid = new ObjectId(myId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid id format");
        }

        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        boolean deleted = journalEntryService.deleteById(oid, userName);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
        }
    }

    // PUT /journal/id/{userName}/{myId}
    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable String userName,
            @PathVariable String myId,
            @RequestBody JournalEntry newEntry) {

        ObjectId oid;
        try {
            oid = new ObjectId(myId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid id format");
        }

        User user = userService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Optional<JournalEntry> maybeOld = journalEntryService.findByObjId(oid);
        if (!maybeOld.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
        }

        JournalEntry updated = journalEntryService.updateJournalById(oid, newEntry);
        return ResponseEntity.ok(updated);
    }
}
