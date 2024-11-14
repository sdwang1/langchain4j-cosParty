package com.leqee.eureka.amphibious.daemon.engine.core;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.daemon.scenario.discuss.FreeDiscussTask;
import com.leqee.eureka.amphibious.daemon.scenario.verdict.VerdictTask;
import com.leqee.eureka.amphibious.navy.Main;
import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousTaskTableRow;
import io.github.sinri.AiOnHttpMix.mirage.MirageSDK;
import io.github.sinri.AiOnHttpMix.mix.AnyLLMKit;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.github.sinri.keel.logger.issue.recorder.KeelIssueRecorder;
import io.github.sinri.keel.servant.queue.KeelQueueTask;
import io.github.sinri.keel.servant.queue.QueueTaskIssueRecord;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public abstract class AbstractAmphibiousTask extends KeelQueueTask {
    private final long taskId;
    private final UnmodifiableJsonifiableEntity taskMeta;
    private final String handlerCode;

    public AbstractAmphibiousTask(EurekaAmphibiousTaskTableRow row) {
        this.taskId = row.getTaskId();
        this.handlerCode = row.getHandlerCode();
        UnmodifiableJsonifiableEntity unmodifiableJsonifiableEntity;
        try {
            unmodifiableJsonifiableEntity = UnmodifiableJsonifiableEntity.wrap(new JsonObject(row.getTaskMeta()));
        } catch (Exception e) {
            unmodifiableJsonifiableEntity = UnmodifiableJsonifiableEntity.wrap(new JsonObject());
        }
        this.taskMeta = unmodifiableJsonifiableEntity;
    }

    public static AbstractAmphibiousTask create(EurekaAmphibiousTaskTableRow row) {
        String handlerCode = row.getHandlerCode();
        return switch (handlerCode) {
            case VerdictTask.HandlerCode -> new VerdictTask(row);
            case FreeDiscussTask.HandlerCode -> new FreeDiscussTask(row);
            default -> new AbstractAmphibiousTask(row) {
                @Override
                public Future<String> executeBody() {
                    return Future.failedFuture("Unsupported handler code: " + handlerCode);
                }
            };
        };
    }

    public UnmodifiableJsonifiableEntity getTaskMeta() {
        return taskMeta;
    }

    @Override
    public Future<Void> run() {
        return Future.succeededFuture()
                .compose(v -> {
                    return executeBody();
                })
                .compose(feedback -> {
                    getIssueRecorder().info("Call updateAmphibiousTask with feedback");
                    return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
                        return new EurekaAmphibiousAction(unionMySQLConnection).updateAmphibiousTask(taskId, true, feedback);
                    });
                }, throwable -> {
                    getIssueRecorder().exception(throwable, "Call updateAmphibiousTask failed");
                    return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
                        return new EurekaAmphibiousAction(unionMySQLConnection).updateAmphibiousTask(taskId, false, throwable.toString());
                    });
                })
                .compose(afx -> {
                    getIssueRecorder().info("Call updateAmphibiousTask afx=" + afx);
                    return Future.succeededFuture();
                });
    }

    @Override
    public @NotNull
    final String getTaskReference() {
        return String.valueOf(taskId);
    }

    @Override
    public @NotNull
    final String getTaskCategory() {
        return this.handlerCode;
    }

    @Override
    protected @NotNull KeelIssueRecorder<QueueTaskIssueRecord> buildIssueRecorder() {
        return Main.getAircraftCarrier().getIssueRecordCenter()
                .generateIssueRecorder(QueueTaskIssueRecord.TopicQueue, () -> {
                    return new QueueTaskIssueRecord(getTaskReference(), getTaskCategory());
                });
    }

    abstract public Future<String> executeBody();

    private MirageSDK buildMirageSDK() {
        var domain = Keel.config("mirage.domain");
        var clientCode = Keel.config("mirage.client_code");
        var clientSecret = Keel.config("mirage.client_secret");

        return new MirageSDK(domain, clientCode, clientSecret);
    }

    protected final AnyLLMKit buildAnyLLMKitThroughMirage(SupportedModel supportedModel) {
        MirageSDK mirageSDK = buildMirageSDK();
//        KeelEventLogger logger = Main.getAircraftCarrier().generateEventLogger("MirageSDKVerbose");
//        logger.setVisibleLevel(KeelLogLevel.DEBUG);
//        mirageSDK.setVerboseLogger(logger);
        return switch (supportedModel.getSeries()) {
            case Qwen -> new AnyLLMKit().useQwen(mirageSDK, supportedModel);
            case Volces -> new AnyLLMKit().useVolces(mirageSDK, supportedModel);
            case ChatGPT -> new AnyLLMKit().useChatGPT(mirageSDK, supportedModel);
        };
    }
}
