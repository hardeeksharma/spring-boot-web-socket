package com.rmehub.chat.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
public class NewChat {
    String sender;
    String type;
    String content;
    Date time;
}
