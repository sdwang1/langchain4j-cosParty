package com.leqee.eureka.amphibious.test.service;

import io.github.sinri.drydock.naval.raider.Privateer;
import io.github.sinri.keel.facade.async.KeelAsyncKit;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractRoutineTest extends Privateer {
    private EurekaAmphibiousClient client;

    abstract protected String passkey();

    abstract protected JsonObject taskMeta();

    @Override
    protected @NotNull Future<Void> prepareEnvironment() {
        client = new EurekaAmphibiousClient(passkey());
        return Future.succeededFuture();
    }

    @TestUnit
    public Future<Void> run() {
        var task_meta = taskMeta();
        return client.createTask(task_meta)
                .compose(task_id -> {
                    getLogger().setDynamicEventLogFormatter(x -> x.classification("#" + task_id));
                    getLogger().info("created task_id:" + task_id);
                    return KeelAsyncKit.repeatedlyCall(routineResult -> {
                        return client.queryTask(task_id)
                                .compose(task -> {
                                    if (Objects.equals(task.getString("task_status"), "FAILED")) {
                                        getLogger().warning("FAILED", task);
                                        routineResult.stop();
                                    } else if (Objects.equals(task.getString("task_status"), "DONE")) {
                                        getLogger().notice("DONE", task);
                                        routineResult.stop();
                                    } else {
                                        getLogger().info("Task Status: " + task.getString("task_status"));
                                    }
                                    return KeelAsyncKit.sleep(3000L);
                                });
                    });
                });
    }
}
