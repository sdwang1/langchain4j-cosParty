package com.leqee.eureka.amphibious.daemon.engine.core;

import dev.langchain4j.data.message.AiMessage;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;

public class ActionRecord {
    private String actorName;
    private AiMessage aiMessage;

    public ActionRecord(@Nonnull String actorName, @Nonnull AiMessage aiMessage) {
        this.actorName = actorName;
        this.aiMessage = aiMessage;
    }

    public String getActorName() {
        return actorName;
    }

    public ActionRecord setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public AiMessage getAiMessage() {
        return aiMessage;
    }

    public ActionRecord setAiMessage(@Nonnull AiMessage aiMessage) {
        this.aiMessage = aiMessage;
        return this;
    }

    public JsonObject toJsonObject(){
        return new JsonObject()
                .put("actor_name", actorName)
                .put("ai_message", new JsonObject()
                        .put("text", aiMessage.text())
                        .put("tool_execution_requests", aiMessage.toolExecutionRequests())
                );
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
