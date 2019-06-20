package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRequestRepository extends MongoRepository<ChatRequest,String> {

    Optional<List<ChatRequest>> findByRequestFromUuid(String s);
    Optional<List<ChatRequest>> findByRequestToUuid(String s);
}
