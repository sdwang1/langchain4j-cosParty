package com.leqee.eureka.amphibious.navy.drone;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.daemon.engine.core.AbstractAmphibiousTask;
import com.leqee.eureka.amphibious.navy.Main;
import io.github.sinri.drydock.air.Drone;
import io.github.sinri.drydock.naval.carrier.AircraftCarrierDeck;
import io.github.sinri.keel.servant.queue.KeelQueue;
import io.github.sinri.keel.servant.queue.KeelQueueTask;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;

public class AmphibiousDrone extends Drone {
    public AmphibiousDrone(@NotNull AircraftCarrierDeck deck) {
        super(deck);
    }

    @Override
    public Future<KeelQueue.QueueSignal> readSignal() {
        return Future.succeededFuture(KeelQueue.QueueSignal.RUN);
    }

    @Override
    public Future<KeelQueueTask> get() {
        return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
            EurekaAmphibiousAction mirageAction = new EurekaAmphibiousAction(unionMySQLConnection);
            return mirageAction.fetchPendingAmphibiousTask()
                    .compose(row -> {
                        if (row == null) {
                            return Future.succeededFuture(null);
                        }
                        AbstractAmphibiousTask amphibiousTask = AbstractAmphibiousTask.create(row);

                        return mirageAction.declarePendingAmphibiousTaskRunning(row.getTaskId())
                                .compose(v -> {
                                    return Future.succeededFuture(amphibiousTask);
                                });
                    });
        });
    }

    @Override
    public int configuredQueueWorkerPoolSize() {
        return 4;
    }

    @Override
    public Future<Void> beforeLoadingQueue() {
        return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
                    return new EurekaAmphibiousAction(unionMySQLConnection).cleanUpRunningAmphibiousTasks();
                })
                .compose(afx -> {
                    getLogger().info("Cleaned up " + afx + " running tasks before loading queue");
                    return Future.succeededFuture();
                });
    }
}
