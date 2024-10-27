package com.maruhxn.tetrisserver.config;

import com.maruhxn.tetrisserver.handler.TetrisSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(tetrisSocketHandler(), "/game")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*"); // 클라이언트에서 웹 소켓 서버에 요청하는 모든 요청을 수락, CORS 방지
        // todo: 실제 서비스에서는 "*"으로 하면 안된다. 스프링에서 웹소켓을 사용할 때, same-origin만 허용하는 것이 기본정책이다.
    }

    @Bean
    public TetrisSocketHandler tetrisSocketHandler() {
        return new TetrisSocketHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        //Text Message의 최대 버퍼 크기 설정
        container.setMaxTextMessageBufferSize(8192);
        //Binary Message의 최대 버퍼 크기 설정
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

}
