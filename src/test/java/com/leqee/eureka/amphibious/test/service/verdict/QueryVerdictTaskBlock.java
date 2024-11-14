package com.leqee.eureka.amphibious.test.service.verdict;

import com.leqee.eureka.amphibious.test.service.EurekaAmphibiousClient;
import io.github.sinri.drydock.naval.raider.Privateer;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

public class QueryVerdictTaskBlock extends Privateer {
    private EurekaAmphibiousClient client;
    @Override
    protected @NotNull Future<Void> prepareEnvironment() {
        client=new EurekaAmphibiousClient("TestVerdictTask");
        return Future.succeededFuture();
    }
    @TestUnit
    public Future<Void> run(){
        var task_id = 7;
        return client.queryTask(task_id)
                .compose(task->{
                    getLogger().info("task",task);
                    return Future.succeededFuture();
                });
    }
}
