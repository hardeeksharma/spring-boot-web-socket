package com.rmehub.chat;

import com.rmehub.chat.interceptor.RmeWebSocketHandShakeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.rmehub.chat.interceptor.RmeSessionChannelInterceptor;
import com.rmehub.chat.interceptor.RmeWebSocketHandShakeInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatWS")// ws connection url
                .setAllowedOrigins("*")
                .setHandshakeHandler(new RmeWebSocketHandShakeHandler())
                .addInterceptors(handShakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration
                .setMessageSizeLimit(5000 * 1024) // Max incoming message size => 5Mo
                .setSendBufferSizeLimit(5000 * 1024); // Max outgoing buffer size => 5Mo
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")// to send data to ws
        .enableSimpleBroker("/queue/", "/topic/", "/exchange/");
        // to subscribe for real time feed
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
