package com.rmehub.chat.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Data
@Document
public class BlockedWord  {

    @Id
    private String id;
    private String word;
}
