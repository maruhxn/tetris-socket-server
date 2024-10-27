package com.maruhxn.tetrisserver.domain;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameRoom {
    private final String roomId;
    private final WebSocketSession player1;
    private final WebSocketSession player2;
    private final Map<String, Object> gameState = new HashMap<>(); // 게임 상태 관리

    public GameRoom(WebSocketSession player1, WebSocketSession player2) {
        this.roomId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = player2;
        // 초기 상태 설정
        gameState.put("player1Score", 0);
        gameState.put("player2Score", 0);
    }

    public String getRoomId() {
        return roomId;
    }

    public WebSocketSession getPlayer1() {
        return player1;
    }

    public WebSocketSession getPlayer2() {
        return player2;
    }

    public Map<String, Object> getGameState() {
        return gameState;
    }

    public WebSocketSession getOpponent(WebSocketSession player) {
        return player.equals(player1) ? player2 : player1;
    }

    public void updateGameState(String key, Object value) {
        gameState.put(key, value);
    }
}

