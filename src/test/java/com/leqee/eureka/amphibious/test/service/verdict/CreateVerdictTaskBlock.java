package com.leqee.eureka.amphibious.test.service.verdict;

import com.leqee.eureka.amphibious.test.service.EurekaAmphibiousClient;
import io.github.sinri.drydock.naval.raider.Privateer;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class CreateVerdictTaskBlock extends Privateer {
    private EurekaAmphibiousClient client;
    @Override
    protected @NotNull Future<Void> prepareEnvironment() {
        client=new EurekaAmphibiousClient("TestVerdictTask");
        return Future.succeededFuture();
    }

    @TestUnit
    public Future<Void> run(){
        var task_meta=new JsonObject()
//                .put("llm_model", SupportedModel.Volces.name())
                .put("accident_description", "ETL集群机器崩溃，导致三小时数据延迟。");
        return client.createTask(task_meta)
                .compose(task_id->{
                    getLogger().info("task_id:"+task_id);
                    return Future.succeededFuture();
                });
    }
}
