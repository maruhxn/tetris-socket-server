package com.maruhxn.tetrisserver.domain.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockActionMessage {

    @JsonProperty("action")
    private ActionType action;

    @JsonProperty("direction")
    private Direction direction;

    @JsonProperty("blockType")
    private String blockType;

    @JsonProperty("position")
    private Position position;

    @JsonProperty("rotation")
    private int rotation;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class Position {
        private int x;
        private int y;
    }
}
