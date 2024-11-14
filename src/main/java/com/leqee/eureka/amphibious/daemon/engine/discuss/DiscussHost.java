package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public class DiscussHost {
    interface Assistant {
        @SystemMessage("你是【{{actorName}}】，是这次讨论会议的召集人。")
        @UserMessage("{{context}}")
        AiMessage chat(@V("actorName") String actorName, @V("context") String context);
    }

    private final ChatLanguageModel llmModel;
    private final String discussIntroduction;
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

    public ActionRecord act(List<ActionRecord> context) {
        StringBuilder sb = new StringBuilder();
        context.forEach(action -> {
            sb.append("【").append(action.getActorName()).append("】说：\n")
                    .append(action.getAiMessage().text()).append("\n");
        });
        sb.append(getConclusionRequest());

        Assistant assistant = AiServices.create(Assistant.class, llmModel);
        AiMessage resMessage = assistant.chat(
                actorName,
                sb.toString()
        );
        return new ActionRecord(
                actorName,
                resMessage
        );
    }

    public ChatLanguageModel getChatLanguageModel() {
        return llmModel;
    }

    public boolean shouldStopDiscussing(List<ActionRecord> context) {
        return true;
    }
}
