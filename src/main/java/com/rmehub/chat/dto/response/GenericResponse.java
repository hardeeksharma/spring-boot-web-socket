package com.rmehub.chat.dto.response;


import com.rmehub.chat.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {

    private int statusCode = 200;
    private boolean isError = false;
    private ResponseCode responseCode;
    private Map<String, Object> payload = new HashMap<>();
    private String message;

}
