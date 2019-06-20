package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepo extends MongoRepository<ChatMessage,String> {
}
