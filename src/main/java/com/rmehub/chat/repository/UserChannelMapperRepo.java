package com.rmehub.chat.repository;

import com.rmehub.chat.model.UserChannelMapper;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserChannelMapperRepo extends MongoRepository<UserChannelMapper, String> {

    Optional<List<UserChannelMapper>> findByUser1UuidOrUser2UuidOrderByModifiedAtDesc(String user1, String user2);
}
