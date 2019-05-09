package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatUserRepository extends MongoRepository<ChatUser,String> {

    Optional<ChatUser> findByUuid(String uuid);
}
