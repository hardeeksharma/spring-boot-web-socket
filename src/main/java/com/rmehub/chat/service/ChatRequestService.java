package com.rmehub.chat.service;

import com.rmehub.chat.constant.RequestStatus;
import com.rmehub.chat.constant.ResponseCode;
import com.rmehub.chat.exception.ChatRequestException;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.model.UserChannelMapper;
import com.rmehub.chat.repository.ChatRequestRepository;
import com.rmehub.chat.repository.ChatUserRepository;
import com.rmehub.chat.repository.UserChannelMapperRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ChatRequestService {

    // mode of payment
    // processor kon gateway kon
    // UPI ?
    // charges? as per card and card type net banking
    // wallet integration
    // how will we validate cash deposit and cheques, imps rtgs etc
    // escrow facility for real estate transaction

    @Autowired
    ChatRequestRepository chatRequestRepository;

    @Autowired
    ChatUserRepository chatUserRepository;

    @Autowired
    ChatChannelService chatChannelService;

    @Autowired
    UserChannelMapperRepo userChannelMapperRepo;

    public Optional<ChatRequest> findByRequestId(String id) {
        return chatRequestRepository.findById(id);
    }

    public ChatRequest save(ChatRequest chatRequest) {

        if (chatRequestRepository.findByRequestToUuidAndRequestFromUuid(chatRequest.getRequestToUuid(), chatRequest.getRequestFromUuid()).isPresent()) {
            return ChatRequest.builder().isDuplicate(true).build();
        }

        return chatRequestRepository.save(chatRequest);
    }

    public ChatChannel acceptOrRejectChatRequest(String chatRequestId, String toUuid, boolean isAccepted, ChatRequest cr) throws ChatRequestException {
        Optional<ChatRequest> request = chatRequestRepository.findById(chatRequestId);

        log.info("acceptOrRejectChatRequest " + cr.toString());
        ChatChannel chatChannel = null;
        if (!request.isPresent()) {
            log.error("No request found for this request ID");
            throw new ChatRequestException("No ChatRequest found", ResponseCode.NOT_FOUND);
        }
        log.info("Chat Request Found");
        ChatRequest chatRequest1 = request.get();
        if (!chatRequest1.getRequestToUuid().equals(toUuid)) {
            log.error("Chat request does not belong to him");
            throw new ChatRequestException("", ResponseCode.CHAT_REQUEST_MISMATCH);
        }
        if (isAccepted) {
            chatRequest1.setAccepted(true);
            chatRequest1.setRequestStatus(RequestStatus.ACCEPTED);
            // if the request is accepted
            // 1st find both the users in the chat users system
            // then create a chat channel between them
            Optional<ChatUser> senderOptional = chatUserRepository.findByUuid(chatRequest1.getRequestFromUuid());
            Optional<ChatUser> receiverOptional = chatUserRepository.findByUuid(chatRequest1.getRequestToUuid());
            // updating the chat request

            StringBuilder channelUuid = new StringBuilder(UUID.randomUUID().toString());

            chatRequest1 = chatRequestRepository.save(chatRequest1);

            StringBuilder partyMd5Hash = new StringBuilder(DigestUtils.md5Hex(senderOptional.get().getUuid() + " : " + receiverOptional.get().getUuid()));

            chatChannel = ChatChannel.builder()
                    .channelId(channelUuid.toString())
                    .sender(senderOptional.get())
                    .receiver(receiverOptional.get())
                    .request(chatRequest1)
                    .partyHash(partyMd5Hash.toString())
                    .build();

            chatChannel = chatChannelService.createChatChannel(chatChannel);

            UserChannelMapper userChannelMapper = UserChannelMapper.builder()
                    .chatChannel(chatChannel)
                    .user1Uuid(senderOptional.get().getUuid())
                    .user2Uuid(receiverOptional.get().getUuid())
                    .build();
            userChannelMapperRepo.save(userChannelMapper);

            log.info("Chat Channel Created " + chatChannel.toString());
            log.info("Request Updated");

        } else {
            chatRequest1.setAccepted(false);
            chatRequest1.setRequestStatus(RequestStatus.REJECTED);
            chatRequest1.setRejectReason(cr.getRejectReason());
            chatRequestRepository.save(chatRequest1);
            log.info("Chat Request rejected : " + chatRequest1.toString());
        }

        return chatChannel;

    }


    public Optional<?> findMySentChatRequest(String uuid) {

        if (chatRequestRepository.findByRequestFromUuid(uuid).get().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(chatRequestRepository.findByRequestFromUuid(uuid));
    }

    public Optional<?> findMyReceivedChatRequest(String uuid) {

        if (chatRequestRepository.findByRequestToUuid(uuid).get().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(chatRequestRepository.findByRequestToUuid(uuid));
    }

    public Optional<?> findMySentReceivedChatRequest(String uuid) {

        if (chatRequestRepository.findByRequestFromUuid(uuid).get().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(chatRequestRepository.findByRequestFromUuid(uuid));
    }
}
