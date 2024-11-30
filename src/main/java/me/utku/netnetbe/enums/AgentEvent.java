package me.utku.netnetbe.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgentEvent {
    START_STREAM("startStream"),
    STOP_STREAM("stopStream");

    private final String event;
}
