package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatChannel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatChannelRepo extends MongoRepository<ChatChannel, String> {

    Optional<ChatChannel> findByChannelId(String channelId);
}
