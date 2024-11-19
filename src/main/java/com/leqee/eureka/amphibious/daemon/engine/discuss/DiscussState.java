package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.AppenderChannel;
import org.bsc.langgraph4j.state.Channel;

import java.util.*;

public class DiscussState extends AgentState {
    public static Map<String, Channel<?>> SCHEMA = Map.of(
            "actor_name", Channel.of(() -> ""),
            "contention", Channel.of(() -> ""),
            "context", AppenderChannel.<ActionRecord>of(ArrayList::new),
            "turn_count", Channel.of(() -> 0)
    );

    public DiscussState(Map<String, Object> initData) {
        super(initData);
    }

    public String getActorName() {
        return this.<String>value("actor_name").orElseThrow();
    }

    public Optional<String> getContention() {
        return value("contention");
    }

    public List<ActionRecord> getContext() {
        return this.<List<ActionRecord>>value("context").orElseGet(ArrayList::new);
    }

    public int getTurnCount() {
        return this.<Integer>value("turn_count").orElse(0);
    }
}
