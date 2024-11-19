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

public class DiscussHost implements Actor {
    interface Assistant {
        @SystemMessage("你是【{{actorName}}】，是这次讨论会议的召集人。")
        @UserMessage("{{context}}")
        AiMessage chat(@V("actorName") String actorName, @V("context") String context);
    }

    private final String discussIntroduction;
    private final ChatLanguageModel llmModel;
    private final String actorName;

    public DiscussHost(ChatLanguageModel llmModel, String actorName, String discussIntroduction) {
        this.llmModel = llmModel;
        this.actorName = actorName;
        this.discussIntroduction = discussIntroduction;
    }

    public String getDiscussIntroduction() {
        return discussIntroduction;
    }

    public String getActorName() {
        return actorName;
    }

    public String getConclusionRequest() {
        return "请根据以上讨论内容，作结束发言。";
    }

    @Override
    public Map<String, Object> act(List<ActionRecord> context) {
        Assistant assistant = AiServices.create(Assistant.class, llmModel);
        AiMessage message = assistant.chat(
                actorName,
                formatContext(context) + getConclusionRequest()
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

    public boolean shouldStopDiscussing(List<ActionRecord> context) { return true; }
}

