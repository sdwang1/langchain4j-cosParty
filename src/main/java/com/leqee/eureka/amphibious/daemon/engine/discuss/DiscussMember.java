package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public class DiscussMember {
    interface Assistant {
        @SystemMessage("你是【{{actorName}}】，正在参加这一场讨论。\n\n" +
                "你的立场、观点和利益相关的信息如下：\n{{contention}}\n\n" +
                "需要根据会话历史继续会话，不要重复之前会话中已有的内容，每次回答不要超过200字。" +
                "讨论的结论将由讨论召集人作出，请不要僭越。\n")
        @UserMessage("{{context}}")
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

    public ActionRecord act(List<ActionRecord> context) {
        StringBuilder sb = new StringBuilder();
        context.forEach(action -> {
            sb.append("【").append(action.getActorName()).append("】说：\n")
                    .append(action.getAiMessage().text()).append("\n");
        });
        sb.append("请作为【").append(getActorName()).append("】继续发表意见。");

        Assistant assistant = AiServices.create(Assistant.class, llmModel);
        AiMessage resMessage = assistant.chat(
                actorName,
                contention,
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
}
