package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatMapper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatMapperRepo extends MongoRepository<ChatMapper, String> {
    Optional<ChatMapper> findByChannelIdOrderByCreationDateDesc(String channelId);
}
