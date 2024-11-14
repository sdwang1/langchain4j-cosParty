package com.leqee.eureka.amphibious.daemon.scenario.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import com.leqee.eureka.amphibious.daemon.engine.discuss.DiscussHost;
import dev.langchain4j.model.chat.ChatLanguageModel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class FreeDiscussHost extends DiscussHost {
    private final int minContextLength;
    private final @Nullable
    String conclusionRequest;

    /**
     * @param minContextLength For N rounds, minContextLength=1+N*MemberCount
     */
    public FreeDiscussHost(
            ChatLanguageModel llmModel,
            String actorName,
            String discussIntroduction,
            int minContextLength,
            @Nullable String conclusionRequest
    ) {
        super(llmModel, actorName, discussIntroduction);
        this.minContextLength = minContextLength;
        this.conclusionRequest = conclusionRequest;
    }

    @Override
    public String getConclusionRequest() {
        return Objects.requireNonNullElseGet(conclusionRequest, super::getConclusionRequest);
    }

    @Override
    public boolean shouldStopDiscussing(List<ActionRecord> context) {
        return context.size() >= minContextLength;
    }
}
