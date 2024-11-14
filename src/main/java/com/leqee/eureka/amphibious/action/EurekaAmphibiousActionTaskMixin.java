package com.leqee.eureka.amphibious.action;

import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousTaskTableRow;
import io.github.sinri.keel.mysql.statement.AnyStatement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface EurekaAmphibiousActionTaskMixin extends EurekaAmphibiousActionMixin {
    default Future<Integer> cleanUpRunningAmphibiousTasks() {
        return AnyStatement.update(updateStatement -> updateStatement
                        .table(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .setWithExpression("end_time", "now()")
                        .setWithValue("feedback", "Terminated due to queue stopped.")
                        .setWithValue("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.FAILED.name())
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsLiteralValue("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.RUNNING.name())
                        )
                )
                .executeForAffectedRows(getNamedSqlConnection());
    }

    default Future<Integer> updateAmphibiousTask(long taskId, boolean done, String feedback) {
        return AnyStatement.update(updateStatement -> updateStatement
                        .table(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .setWithExpression("end_time", "now()")
                        .setWithValue("feedback", feedback)
                        .setWithValue("task_status", (done ? EurekaAmphibiousTaskTableRow.TaskStatusEnum.DONE : EurekaAmphibiousTaskTableRow.TaskStatusEnum.FAILED).name())
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsNumericValue("task_id", taskId)
                        )
                        .limit(1)
                )
                .executeForAffectedRows(getNamedSqlConnection());
    }

    default Future<EurekaAmphibiousTaskTableRow> fetchPendingAmphibiousTask() {
        return AnyStatement.select(selectStatement -> selectStatement
                        .from(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsLiteralValue("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.PENDING.name())
                        )
                        .orderByAsc("apply_time")
                        .limit(1)
                )
                .queryForOneRow(getNamedSqlConnection(), EurekaAmphibiousTaskTableRow.class);
    }

    default Future<Void> declarePendingAmphibiousTaskRunning(long taskId) {
        return AnyStatement.update(updateStatement -> updateStatement
                        .table(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .setWithExpression("start_time", "now()")
                        .setWithValue("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.RUNNING.name())
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsNumericValue("task_id", taskId)
                                .expressionEqualsLiteralValue("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.PENDING.name())
                        )
                        .limit(1)
                )
                .executeForAffectedRows(getNamedSqlConnection())
                .compose(afx -> {
                    if (afx == 1) {
                        return Future.succeededFuture();
                    } else {
                        return Future.failedFuture(new RuntimeException("Task already running or not pending."));
                    }
                });
    }

    default Future<Long> createEurekaAmphibiousTask(String handlerCode, JsonObject taskMeta) {
        return AnyStatement.insert(writeIntoStatement -> writeIntoStatement
                        .intoTable(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .macroWriteOneRow(row -> row
                                .put("handler_code", handlerCode)
                                .put("task_meta", taskMeta.toString())
                                .putNow("apply_time")
                                .put("task_status", EurekaAmphibiousTaskTableRow.TaskStatusEnum.PENDING.name())
                        )
                )
                .executeForLastInsertedID(getNamedSqlConnection());
    }

    default Future<EurekaAmphibiousTaskTableRow> queryEurekaAmphibiousTask(long taskId) {
        return AnyStatement.select(selectStatement -> selectStatement
                        .from(EurekaAmphibiousTaskTableRow.SCHEMA_AND_TABLE)
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsLiteralValue("task_id", taskId))
                        .limit(1)
                )
                .queryForOneRow(getNamedSqlConnection(), EurekaAmphibiousTaskTableRow.class);
    }
}
