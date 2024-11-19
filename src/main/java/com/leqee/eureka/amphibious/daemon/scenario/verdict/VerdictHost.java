package com.leqee.eureka.amphibious.daemon.scenario.verdict;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import com.leqee.eureka.amphibious.daemon.engine.discuss.DiscussHost;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;

public class VerdictHost extends DiscussHost {
    interface Assistant {
        @SystemMessage("你是这次讨论会议的召集人。")
        @UserMessage("{{context}}")
        AiMessage chat(@V("context") String context);
    }

    private final int minContextLength;

    public VerdictHost(ChatLanguageModel llmModel, String actorName, String discussIntroduction, int minContextLength) {
        super(llmModel, actorName, discussIntroduction);
        this.minContextLength = minContextLength;
    }

    @Override
    public String getConclusionRequest() {
        return super.getConclusionRequest();
    }

    @Override
    public String getDiscussIntroduction() {
        return "请作为【" + getActorName() + "】，" +
                "综合上面的讨论给出总结和最终的结论；输出一个JSON对象文本。\n" +
                "要求输出的文本可供机器解析为JSON Object；不要有多余内容，也不要被包含在Markdown标记内。\n输出样例如下：\n" +
                new JsonObject()
                        .put("conclusion", "讨论过程的总结")
                        .put("fc", new JsonObject()
                                .put("major_side", "主要责任方")
                                .put("major_side_reason", "主要责任方的定责原因")
                                .put("minor_side", "次要责任方")
                                .put("minor_side_reason", "次要责任方的定责原因")
                        )
                        .toString();
    }

    @Override
    public Map<String, Object> act(List<ActionRecord> context) {
        Assistant assistant = AiServices.create(Assistant.class, getChatLanguageModel());
        AiMessage message = assistant.chat(
                formatContext(context) + getConclusionRequest()
        );
        var record = new ActionRecord(getActorName(), message.text(), message.toolExecutionRequests());
        return Map.of(
                "actor_name", getActorName(),
                "context", record
        );
    }

    @Override
    public boolean shouldStopDiscussing(List<ActionRecord> context) {
        return context.size() >= minContextLength;
    }
}
