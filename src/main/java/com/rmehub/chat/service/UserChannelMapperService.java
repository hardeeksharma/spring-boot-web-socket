package com.rmehub.chat.service;

import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.exception.ChatRequestException;
import com.rmehub.chat.model.UserChannelMapper;
import com.rmehub.chat.repository.UserChannelMapperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserChannelMapperService {

    @Autowired
    UserChannelMapperRepo repo;

   public List<UserChannelMapper> findMyChannels(String uuid) throws ChatRequestException {
        Optional<List<UserChannelMapper>> optional = repo.findByUser1UuidOrUser2UuidOrderByModifiedAtDesc(uuid, uuid);
        if (!optional.isPresent())
            throw new ChatRequestException("", ResponseCode.NOT_FOUND);

        return optional.get();
    }

}
