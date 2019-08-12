package com.rmehub.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class UserChannelMapper {

    @Id
    String id;

    @DBRef
    ChatChannel chatChannel;

    @Indexed
    String user1Uuid;

    @Indexed
    String user2Uuid;

    @CreatedDate
    Date createdAt;

    @LastModifiedDate
    Date modifiedAt;

}
