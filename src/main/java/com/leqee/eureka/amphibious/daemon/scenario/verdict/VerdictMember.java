package com.leqee.eureka.amphibious.daemon.scenario.verdict;

import com.leqee.eureka.amphibious.daemon.engine.discuss.DiscussMember;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class VerdictMember extends DiscussMember {
    public VerdictMember(ChatLanguageModel llmModel, String actorName, String contention) {
        super(llmModel, actorName, contention);
    }
}
