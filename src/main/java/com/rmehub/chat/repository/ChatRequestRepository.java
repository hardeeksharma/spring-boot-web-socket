package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRequestRepository extends MongoRepository<ChatRequest,String> {
}
