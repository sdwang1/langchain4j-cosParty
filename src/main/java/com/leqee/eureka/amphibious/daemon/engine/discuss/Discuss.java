package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import dev.langchain4j.data.message.AiMessage;

import java.util.ArrayList;
import java.util.List;

public class Discuss {
    private final DiscussHost host;
    private final List<? extends DiscussMember> members;
    private final List<ActionRecord> context;


    public Discuss(
            DiscussHost host,
            List<? extends DiscussMember> members
    ) {
        this.host = host;
        this.members = members;
        context = new ArrayList<>();
    }

    public void run() {
        var start = new ActionRecord(host.getActorName(), AiMessage.aiMessage(host.getDiscussIntroduction()));
        context.add(start);

        do {
            members.forEach(member -> context.add(member.act(context)));
        } while (!host.shouldStopDiscussing(context));
        context.add(host.act(context));
    }

    public List<ActionRecord> getContext() {
        return context;
    }
}
