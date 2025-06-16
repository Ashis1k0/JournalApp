package com.ashis.journalApp.controller;

import com.ashis.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

        private Map<Long, JournalEntry> journalEntries = new HashMap<>();

        @GetMapping
        public List<JournalEntry> getAll() // localhost:8080/journal -->(GET) as we haven't added a end point for this mapping; so for this url call when we call get we will reach here
        {
            return new ArrayList<>(journalEntries.values());
        }

        @PostMapping
        public boolean createEntry(@RequestBody JournalEntry myEntry) // localhost:8080/journal -->(POST)if do post then will reach here
        {
            journalEntries.put(myEntry.getId(),myEntry);
            return true;
        }

        @GetMapping("id/{myId}") //get entry by using id localhost:8080/journal/id/2(myId)
        public JournalEntry getJournalEntryById(@PathVariable Long myId)
        {
            return journalEntries.get(myId);
        }

    @DeleteMapping("id/{myId}") //delete enrty by using it's id  localhost:8080/journal/id/2(myId)
    public JournalEntry deleteJournalEntryById(@PathVariable Long myId)
    {
        return journalEntries.remove(myId);
    }

    @PutMapping ("id/{myId}") //update enrty by using it's id  localhost:8080/journal/id/2(myId)
    public JournalEntry updateJournalEntryById(@PathVariable Long myId ,@RequestBody JournalEntry myEntry)
    {
        return journalEntries.put(myId,myEntry);
    }
}
