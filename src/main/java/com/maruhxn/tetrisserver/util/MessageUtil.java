package com.maruhxn.tetrisserver.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maruhxn.tetrisserver.domain.message.BlockActionMessage;

public class MessageUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(BlockActionMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }

    public static BlockActionMessage fromJson(String json) throws Exception {
        return objectMapper.readValue(json, BlockActionMessage.class);
    }
}