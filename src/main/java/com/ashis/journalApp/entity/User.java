package com.ashis.journalApp.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection ="users") //this will map the class to "users" named collection
@Data
public class User
{
    @Id //map as primary key
    private ObjectId id;
    @Indexed(unique = true) //helps use to keep your user name unique
    // this indexing not happens automatically we have to do it manually.but by using spring boot we can do it --> spring.data.mongodb.auto-index-creation=true  (Add this to app.properties)
    @NonNull //it can't be null
    private String userName;
    @NonNull
    private String password;
    @DBRef //this helps to take a ref of journal entries for each user in collection "journal_entries"
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
