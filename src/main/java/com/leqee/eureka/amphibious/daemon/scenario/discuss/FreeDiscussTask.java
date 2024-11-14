package com.leqee.eureka.amphibious.daemon.scenario.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.AbstractAmphibiousTask;
import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import com.leqee.eureka.amphibious.daemon.engine.discuss.Discuss;
import com.leqee.eureka.amphibious.daemon.engine.discuss.DiscussHost;
import com.leqee.eureka.amphibious.daemon.engine.discuss.DiscussMember;
import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousTaskTableRow;
import dev.langchain4j.model.chat.ChatLanguageModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class FreeDiscussTask extends AbstractAmphibiousTask {
    public static final String HandlerCode = "FreeDiscussTask";

    public FreeDiscussTask(EurekaAmphibiousTaskTableRow row) {
        super(row);
    }

    @Override
    public Future<String> executeBody() {
        FreeDiscussTaskMeta freeDiscussTaskMeta = new FreeDiscussTaskMeta(this.getTaskMeta());
        ChatLanguageModel llmModel = freeDiscussTaskMeta.getLLMModel();

        FreeDiscussTaskMeta.HostMeta hostMeta = freeDiscussTaskMeta.getHostMeta();
        List<FreeDiscussTaskMeta.MemberMeta> memberMetaList = freeDiscussTaskMeta.getMemberMetaList();
        DiscussHost host = new FreeDiscussHost(
                llmModel,
                hostMeta.getHostName(),
                hostMeta.getDiscussIntroduction(),
                hostMeta.getRound() * memberMetaList.size() + 1,
                hostMeta.getConclusionRequest()
        );
        List<DiscussMember> members = new ArrayList<>();
        memberMetaList.forEach(memberMeta -> {
            DiscussMember member = new DiscussMember(llmModel, memberMeta.getActorName(), memberMeta.getContention());
            members.add(member);
        });
        Discuss discuss = new Discuss(host, members);

        discuss.run();
        List<ActionRecord> actionRecords = discuss.getContext();

        JsonArray array = new JsonArray();
        actionRecords.forEach(actionRecord -> {
            array.add(actionRecord.toJsonObject());
        });
        return Future.succeededFuture(array.toString());
    }
}
