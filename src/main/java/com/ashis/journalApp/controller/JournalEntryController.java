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


        @GetMapping("{userName}")
        public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) // localhost:8080/journal -->(GET) as we haven't added a end point for this mapping; so for this url call when we call get we will reach here
        {
            User user = userService.findByUserName(userName);
            List<JournalEntry> journalEntryServiceAll = user.getJournalEntries();
            if(journalEntryServiceAll != null && !journalEntryServiceAll.isEmpty())
            {
                return new ResponseEntity<>(journalEntryServiceAll, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
            }
        }



        @PostMapping({"userName"})
        public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry ,@PathVariable String userName) // localhost:8080/journal -->(POST)if do post then will reach here
        {
            try {
                //first Find user
                journalEntryService.saveEntry(myEntry,userName);
                return new ResponseEntity<>(myEntry, HttpStatus.CREATED); //succesfully created a entry
            }catch (Exception e)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }



        @GetMapping("id/{myId}") //get entry by using id localhost:8080/journal/id/(myId)
        public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId)
        {
            Optional<JournalEntry> journalEntry = journalEntryService.findByObjId(myId);
            //if obj present in optional
            if(journalEntry.isPresent())
            {
                return new ResponseEntity<JournalEntry>(journalEntry.get(), HttpStatus.OK);
            }
            // if req not found
            else{
                return new ResponseEntity<JournalEntry>(HttpStatus.NOT_FOUND);
            }
        }



    @DeleteMapping("id/{userName}/{myId}") //delete enrty by using it's id  localhost:8080/journal/id/2(myId)
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId ,@PathVariable String userName)
    {
        journalEntryService.deleteById(myId,userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //successfully deleted
    }



    @PutMapping ("id/{userName}/{myId}") //update enrty by using it's id  localhost:8080/journal/id/2(myId)
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myId ,
            @RequestBody JournalEntry newEntry ,
            @PathVariable String userName)
    {
        JournalEntry oldEntry = journalEntryService.findByObjId(myId).orElse(null); //stores the prevEntry
        //if oldEnty is noot null
        if(oldEntry != null)
        {
            //check if oldEnty->title != null && it's !equals to "" ;then we will update the newTitle otherwise let it be as prevTitle
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());

            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry,HttpStatus.OK);
        }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }
}
