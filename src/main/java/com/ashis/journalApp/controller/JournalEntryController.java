package com.ashis.journalApp.controller;

import com.ashis.journalApp.entity.JournalEntry;
import com.ashis.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

        @Autowired
        private JournalEntryService journalEntryService;

        @GetMapping
        public List<JournalEntry> getAll() // localhost:8080/journal -->(GET) as we haven't added a end point for this mapping; so for this url call when we call get we will reach here
        {
            return journalEntryService.getAll();
        }

        @PostMapping
        public JournalEntry createEntry(@RequestBody JournalEntry myEntry) // localhost:8080/journal -->(POST)if do post then will reach here
        {
            myEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntry);
            return myEntry;
        }

        @GetMapping("id/{myId}") //get entry by using id localhost:8080/journal/id/(myId)
        public JournalEntry getJournalEntryById(@PathVariable ObjectId myId)
        {
            return journalEntryService.findByObjId(myId).orElse(null);
        }

    @DeleteMapping("id/{myId}") //delete enrty by using it's id  localhost:8080/journal/id/2(myId)
    public boolean deleteJournalEntryById(@PathVariable ObjectId myId)
    {
        journalEntryService.deleteById(myId);
        return true;
    }

    @PutMapping ("id/{myId}") //update enrty by using it's id  localhost:8080/journal/id/2(myId)
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId myId ,@RequestBody JournalEntry newEntry)
    {
        JournalEntry oldEntry = journalEntryService.findByObjId(myId).orElse(null); //stores the prevEntry
        //if oldEnty is noot null
        if(oldEntry != null)
        {
            //check if oldEnty->title != null && it's !equals to "" ;then we will update the newTitle otherwise let it be as prevTitle
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
        }
        journalEntryService.saveEntry(oldEntry);
        return oldEntry;
    }
}
