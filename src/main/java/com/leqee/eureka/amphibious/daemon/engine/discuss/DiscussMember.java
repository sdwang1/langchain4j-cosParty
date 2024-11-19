package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import com.leqee.eureka.amphibious.daemon.engine.core.Actor;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;
import java.util.Map;

public class DiscussMember implements Actor {
    interface Assistant {
        @SystemMessage("你是【{{actorName}}】，正在参加这一场讨论。\n\n" +
                "你的立场、观点和利益相关的信息如下：\n{{contention}}\n\n" +
                "需要根据会话历史继续会话，不要重复之前会话中已有的内容，每次回答不要超过200字。" +
                "讨论的结论将由讨论召集人作出，请不要僭越。\n")
        @UserMessage("""
        目前的会话历史:
        {{context}}
        """)
        AiMessage chat(@V("actorName") String actorName, @V("contention") String contention, @V("context") String context);
    }

    private final String actorName;
    private final String contention;
    private final ChatLanguageModel llmModel;

    public DiscussMember(ChatLanguageModel llmModel, String actorName, String contention) {
        this.llmModel = llmModel;
        this.actorName = actorName;
        this.contention = contention;
    }

    public String getContention() {
        return contention;
    }

    public String getActorName() {
        return actorName;
    }

    @Override
    public Map<String, Object> act(List<ActionRecord> context) {
        Assistant assistant = AiServices.create(Assistant.class, llmModel);
        AiMessage message = assistant.chat(
                actorName,
                contention,
                formatContext(context) + "请作为【" + actorName + "】继续发表意见。"
        );
        var record = new ActionRecord(actorName, message.text(), message.toolExecutionRequests());
        return Map.of(
                "actor_name", actorName,
                "context", record
        );
    }

    public ChatLanguageModel getChatLanguageModel() {
        return llmModel;
    }
}

