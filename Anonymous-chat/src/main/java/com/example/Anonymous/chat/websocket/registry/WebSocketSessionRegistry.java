package com.example.Anonymous.chat.websocket.registry;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionRegistry {

    private final Map<String, WebSocketSession> sessionsByPeerId = new ConcurrentHashMap<>();

    public void register(String peerId, WebSocketSession session) {
        sessionsByPeerId.put(peerId, session);
    }

    public void unregister(String peerId) {
        sessionsByPeerId.remove(peerId);
    }

    public Optional<WebSocketSession> find(String peerId) {
        return Optional.ofNullable(sessionsByPeerId.get(peerId));
    }

    public int size() {
        return sessionsByPeerId.size();
    }
}