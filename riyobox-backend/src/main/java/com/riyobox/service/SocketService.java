package com.riyobox.service;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocketService {

    private static final Logger log = LoggerFactory.getLogger(SocketService.class);
    private final SocketIOServer server;

    @PostConstruct
    public void start() {
        server.addConnectListener(client -> {
            log.info("Client connected: {}", client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            log.info("Client disconnected: {}", client.getSessionId());
        });

        server.addEventListener("user-activity", Map.class, (client, data, ackSender) -> {
            log.info("User activity: {}", data);
            server.getBroadcastOperations().sendEvent("user-activity", data);
        });

        server.start();
        log.info("Socket.io server started on port {}", server.getConfiguration().getPort());
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }

    public void broadcastMovieAdded(Object movie) {
        server.getBroadcastOperations().sendEvent("movie-added", movie);
    }

    public void broadcastStats(Map<String, Object> stats) {
        server.getBroadcastOperations().sendEvent("stats-update", stats);
    }
}
