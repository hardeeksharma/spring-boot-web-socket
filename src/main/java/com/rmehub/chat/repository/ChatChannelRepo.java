package com.rmehub.chat.repository;

import com.rmehub.chat.model.ChatChannel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatChannelRepo extends MongoRepository<ChatChannel, String> {

    Optional<ChatChannel> findByChannelId(String channelId);

    Optional<ChatChannel> findByPartyHash(String hash);

    Optional<List<ChatChannel>> findBySender_UuidOrReceiver_UuidOrderByLastChatTimeStampDesc(String suuid, String ruuid);
}
