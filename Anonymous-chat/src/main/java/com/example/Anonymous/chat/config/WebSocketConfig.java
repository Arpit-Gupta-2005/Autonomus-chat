package com.example.Anonymous.chat.config;

import com.example.Anonymous.chat.websocket.handler.ChatWebSocketHandler;
import com.example.Anonymous.chat.websocket.interceptor.PeerIdHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableConfigurationProperties(WebSocketProperties.class)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final PeerIdHandshakeInterceptor peerIdHandshakeInterceptor;
    private final WebSocketProperties webSocketProperties;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(chatWebSocketHandler, webSocketProperties.getPath())
                .addInterceptors(peerIdHandshakeInterceptor)
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins());
    }
}
