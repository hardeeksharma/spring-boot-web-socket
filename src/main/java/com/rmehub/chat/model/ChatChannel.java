package com.rmehub.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class ChatChannel {

    @Id
    private String id;

    @DBRef
    private ChatRequest request;

    @Indexed
    private String channelId; // Create Random Chat channel ID

    private ChatUser sender;

    private ChatUser receiver;

    private Date lastChatTimeStamp;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    // This will be used to check if channel exist or not for both parties
    private String partyHash;

}
