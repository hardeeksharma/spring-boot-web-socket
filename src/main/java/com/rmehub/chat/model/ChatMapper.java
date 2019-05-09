package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class ChatMapper {

    @Id
    private String id;

    @DBRef
    ChatMessage message;

    @Indexed
    String channelId;

    Date creationDate;

}
