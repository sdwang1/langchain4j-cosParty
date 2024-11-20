package com.leqee.eureka.amphibious.daemon.engine.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import dev.langchain4j.internal.ValidationUtils;
import io.vertx.core.Future;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

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
        ValidationUtils.ensureNotEmpty(members, "members");
        context = new ArrayList<>();
    }

    public Future<Void> run() {
        CompiledGraph<DiscussState> workflow;
        String hostActorName = host.getActorName();
        try {
            StateGraph<DiscussState> graph = new StateGraph<>(DiscussState.SCHEMA, DiscussState::new)
                    .addEdge(START, hostActorName)
                    .addNode(hostActorName, node_async(state -> {
                        var history = state.getContext();
                        if (history.isEmpty()) {
                            var start = new ActionRecord(hostActorName, host.getDiscussIntroduction());
                            return Map.of(
                                    "actor_name", hostActorName,
                                    "context", start
                            );
                        } else {
                            context.addAll(history);
                            var map = host.act(history);
                            context.add((ActionRecord) map.get("context"));
                            return map;
                        }
                    }));
            for (int i = 0; i < members.size(); ++i) {
                var member = members.get(i);
                String actorName = member.getActorName();
                graph = graph.addNode(actorName, node_async(state -> {
                    return member.act(state.getContext());
                }));
                if (i > 0) {
                    graph = graph.addEdge(members.get(i - 1).getActorName(), actorName);
                }
            }
            var firstActorName = members.get(0).getActorName();
            var lastActorName = members.get(members.size() - 1).getActorName();
            graph = graph
                    .addConditionalEdges(
                            hostActorName,
                            edge_async(state -> host.shouldStopDiscussing(state.getContext()) ? END : firstActorName),
                            Map.of(firstActorName, firstActorName, END, END))
                    .addConditionalEdges(
                            lastActorName,
                            edge_async(state -> host.shouldStopDiscussing(state.getContext()) ? hostActorName : firstActorName),
                            Map.of(firstActorName, firstActorName, hostActorName, hostActorName));
            workflow = graph.compile();
        } catch (GraphStateException e) {
            return Future.failedFuture(e);
        }

        Map<String, Object> initialState = new HashMap<>();
        initialState.put("actor_name", hostActorName);
        initialState.put("context", new ArrayList<>());
        try {
            workflow.invoke(initialState);
            return Future.succeededFuture();
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }

    public List<ActionRecord> getContext() {
        return context;
    }
}

