package com.rmehub.chat;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.rmehub.chat.interceptor.RmeSessionChannelInterceptor;
import com.rmehub.chat.interceptor.RmeWebSocketHandShakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chatWS")// ws connection url
				.setAllowedOrigins("*")
				.addInterceptors(handShakeInterceptor())
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app")// to send data to ws
				.enableSimpleBroker("/topic"); // to subscribe for real time feed
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(rmeSessionChannelInterceptor());
	}

	@Bean
	public RmeSessionChannelInterceptor rmeSessionChannelInterceptor() {
		return new RmeSessionChannelInterceptor();
	}
	
	@Bean
	public RmeWebSocketHandShakeInterceptor handShakeInterceptor() {
		return new RmeWebSocketHandShakeInterceptor();
	}

	
}
