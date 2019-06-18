package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatChannel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatChannelRepo extends MongoRepository<ChatChannel,String> {

}
