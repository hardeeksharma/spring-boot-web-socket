package com.rmehub.chat.responseDto;

import com.rmehub.chat.model.ChatMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponseDto {

    List<ChatMapper> mapper = new ArrayList<>();
    long totalElements;
    int noOfPages;


}
