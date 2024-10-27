package com.maruhxn.tetrisserver.handler;

import com.maruhxn.tetrisserver.domain.GameRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class TetrisSocketHandler extends TextWebSocketHandler {
    private final Queue<WebSocketSession> queue = new ConcurrentLinkedQueue<>();
    private Map<String, GameRoom> gameRooms = new HashMap<>();


    //websocket handshake가 완료되어 연결이 수립될 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("session connected: {}", session.getId());
        queue.add(session);
        if (queue.size() >= 2) {
            // 두 명이 모이면 게임 시작
            WebSocketSession player1 = queue.poll();
            WebSocketSession player2 = queue.poll();
            GameRoom gameRoom = new GameRoom(player1, player2);
            gameRooms.put(gameRoom.getRoomId(), gameRoom);
            startGame(gameRoom);
        }
    }

    private void startGame(GameRoom gameRoom) throws IOException {
        try {
            String startMessage = "{\"action\": \"START_GAME\", \"roomId\": \"" + gameRoom.getRoomId() + "\"}";
            gameRoom.getPlayer1().sendMessage(new TextMessage(startMessage));
            gameRoom.getPlayer2().sendMessage(new TextMessage(startMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = findGameRoomByPlayer(session);
        if (roomId == null) {
            return;
        }

        GameRoom gameRoom = gameRooms.get(roomId);
        WebSocketSession opponent = gameRoom.getOpponent(session);

        try {
            // 상대방에게만 메시지 전송
            opponent.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findGameRoomByPlayer(WebSocketSession session) {
        return gameRooms.values().stream()
                .filter(room -> room.getPlayer1().equals(session) || room.getPlayer2().equals(session))
                .map(GameRoom::getRoomId)
                .findFirst()
                .orElse(null);
    }

    //websocket 오류가 발생했을 때 호출
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("session disconnected: {}", session.getId());

        queue.remove(session);
        gameRooms.values().removeIf(room -> room.getPlayer1().equals(session) || room.getPlayer2().equals(session));
    }

    //websocket 세션 연결이 종료되었을 때 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("session disconnected: {}", session.getId());

        queue.remove(session);
        gameRooms.values().removeIf(room -> room.getPlayer1().equals(session) || room.getPlayer2().equals(session));
    }
}
