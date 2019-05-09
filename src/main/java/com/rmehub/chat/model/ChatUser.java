package com.rmehub.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {

    @Id
    private String id;

    @Indexed
    private String uuid;
    private String fullName;

    private Date lastOnline;

    private boolean isOnline;
    private int profanityCount;

}
