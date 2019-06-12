package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class ChatChannel {

    @Id
    private String id;

    @DBRef
    private ChatRequest request;

    @Indexed
    private String channelId; // Create Random Chat channel ID

    @DBRef
    private ChatUser user1;

    @DBRef
    private ChatUser user2;

    private Date lastChatTimeStamp;


}
