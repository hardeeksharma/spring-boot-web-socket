package com.rmehub.chat.service;

import com.rmehub.chat.constant.RequestStatus;
import com.rmehub.chat.model.ChatChannel;
import com.rmehub.chat.model.ChatRequest;
import com.rmehub.chat.model.ChatUser;
import com.rmehub.chat.repository.ChatRequestRepository;
import com.rmehub.chat.repository.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public ChatRequest save(ChatRequest chatRequest) {
        return chatRequestRepository.save(chatRequest);
    }

    public ChatChannel acceptOrRejectChatRequest(String chatRequestId, String toUuid, boolean isAccepted) {
        Optional<ChatRequest> request = chatRequestRepository.findById(chatRequestId);
        ChatChannel chatChannel = null;
        if (!request.isPresent()) {
            log.error("No request found for this request ID");
            // TODO handle chat request not found by ID
        }
        log.info("Chat Request Found");
        ChatRequest chatRequest1 = request.get();
        if (!chatRequest1.getRequestToUuid().equals(toUuid)) {
            //TODO this chat ID is not his chat Request
            log.error("Chat request does not belong to him");
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
            log.info("Chat Channel Created " + chatChannel.toString());
            log.info("Request Updated");

        } else {
            chatRequest1.setAccepted(false);
            chatRequest1.setRequestStatus(RequestStatus.REJECTED);
            chatRequestRepository.save(chatRequest1);

            // not updating because status is already false
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

}
