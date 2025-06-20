package com.ashis.journalApp.repository;

import com.ashis.journalApp.entity.JournalEntry;
import com.ashis.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
                                                    //pojo -- user entityClass
public interface UserRepository extends MongoRepository<User, ObjectId>
{
    User findByUserName(String username);
}
