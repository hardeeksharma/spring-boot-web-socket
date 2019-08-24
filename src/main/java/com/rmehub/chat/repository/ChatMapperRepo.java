package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMapperRepo extends MongoRepository<ChatMapper, String> {
    Page<ChatMapper> findByChannelIdOrderByCreationDateDesc(String channelId, Pageable page);
}
