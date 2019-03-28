package com.rmehub.chat.interceptor;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class RmeSessionChannelInterceptor implements ChannelInterceptor {

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		System.out.println("Channel Interceptor");
		Map<String, Object> sessionHeaders = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
		String sessionId = (String) sessionHeaders.get("session.id");
		
		System.out.println(sessionId);

		return message;
	}

}
