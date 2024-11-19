package com.leqee.eureka.amphibious.daemon.engine.core;

import java.util.List;
import java.util.Map;

public interface Actor {
    String getActorName();
    Map<String, Object> act(List<ActionRecord> context);

    default String formatContext(List<ActionRecord> context) {
        StringBuilder sb = new StringBuilder();
        context.forEach(action -> {
            sb.append("【")
                    .append(action.getActorName())
                    .append("】说：\n")
                    .append(action.getContent())
                    .append("\n");
        });
        return sb.toString();
    }
}
