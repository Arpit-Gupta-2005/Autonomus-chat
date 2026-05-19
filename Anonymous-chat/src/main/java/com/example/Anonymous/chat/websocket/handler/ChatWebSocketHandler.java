package com.example.Anonymous.chat.websocket.handler;

import com.example.Anonymous.chat.websocket.interceptor.PeerIdHandshakeInterceptor;
import com.example.Anonymous.chat.websocket.registry.WebSocketSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionRegistry sessionRegistry;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String peerId = getPeerId(session);
        sessionRegistry.register(peerId, session);
        session.sendMessage(new TextMessage("{\"type\":\"CONNECTED\",\"peerId\":\"" + peerId + "\"}"));
        log.info("WebSocket connected peerId={} activeSessions={}", peerId, sessionRegistry.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String peerId = getPeerId(session);
        log.debug("Message from peerId={} payload={}", peerId, message.getPayload());
        // Step 4 will route signaling; for now echo for manual testing
        session.sendMessage(new TextMessage("{\"type\":\"ECHO\",\"peerId\":\"" + peerId + "\"}"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String peerId = getPeerId(session);
        sessionRegistry.unregister(peerId);
        log.info("WebSocket closed peerId={} status={} activeSessions={}", peerId, status, sessionRegistry.size());
    }

    private String getPeerId(WebSocketSession session) {
        return (String) session.getAttributes().get(PeerIdHandshakeInterceptor.PEER_ID_ATTR);
    }
}
