package com.leqee.eureka.amphibious.daemon.engine.core;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;

public class ActionRecord implements Serializable {
    private String actorName;
    private String content;
    private List<ToolExecutionRequest> toolExecutionRequests;

    public ActionRecord(@Nonnull String actorName, @Nonnull String content, @Nullable List<ToolExecutionRequest> toolExecutionRequests) {
        this.actorName = actorName;
        this.content = content;
        this.toolExecutionRequests = toolExecutionRequests;
    }
    public ActionRecord(@Nonnull String actorName, @Nonnull String content){
        this(actorName, content, null);
    }

    public String getActorName() {
        return actorName;
    }

    public ActionRecord setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ActionRecord setContent(String content) {
        this.content = content;
        return this;
    }

    public List<ToolExecutionRequest> getToolExecutionRequests() {
        return toolExecutionRequests;
    }

    public ActionRecord setToolExecutionRequests(List<ToolExecutionRequest> toolExecutionRequests) {
        this.toolExecutionRequests = toolExecutionRequests;
        return this;
    }

    public JsonObject toJsonObject(){
        var j= new JsonObject()
                .put("actor_name", actorName)
                .put("content", content);
        if(toolExecutionRequests != null && !toolExecutionRequests.isEmpty()) {
            j.put("tool_execution_requests", toolExecutionRequests);
        }
        return j;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }
}
