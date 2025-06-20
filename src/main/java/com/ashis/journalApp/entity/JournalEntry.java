package com.ashis.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection ="journal_entries") //this will map the class to "journal_entries" named collection
//these annotations help to add getters & setters using lombok during compilation
@Data
@NoArgsConstructor //as @Data don't have it
public class JournalEntry
{
    @Id //map as primary key
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
}
