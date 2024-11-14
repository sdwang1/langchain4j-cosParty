package com.leqee.eureka.amphibious.table.union.kumori;
import io.github.sinri.keel.mysql.matrix.AbstractTableRow;
import io.vertx.core.json.JsonObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Table comment is empty.
 * (´^ω^`)
 * SCHEMA: kumori
 * TABLE: eureka_amphibious_task
 * (*￣∇￣*)
 * NOTICE BY KEEL:
 * 	To avoid being rewritten, do not modify this file manually, unless editable confirmed.
 * 	It was auto-generated on Mon Oct 14 15:13:43 CST 2024.
 * @see io.github.sinri.keel.mysql.dev.TableRowClassSourceCodeGenerator
 */
public class EurekaAmphibiousTaskTableRow extends AbstractTableRow {
    public static final String SCHEMA_AND_TABLE = "kumori.eureka_amphibious_task";

    public EurekaAmphibiousTaskTableRow(JsonObject tableRow) {
        super(tableRow);
    }

    @Override
    @Nonnull
    public String sourceTableName() {
        return "eureka_amphibious_task";
    }

    public String sourceSchemaName(){
        return "kumori";
    }
    /*
     * Field `task_id` of type `bigint(20) unsigned`.
     */
    @Nonnull
    public Long getTaskId() {
        return Objects.requireNonNull(readLong("task_id"));
    }

    /*
     * Field `handler_code` of type `varchar(200)`.
     */
    @Nonnull
    public String getHandlerCode() {
        return Objects.requireNonNull(readString("handler_code"));
    }

    /*
     * Field `task_meta` of type `mediumtext`.
     */
    @Nonnull
    public String getTaskMeta() {
        return Objects.requireNonNull(readString("task_meta"));
    }

    /*
     * Field `apply_time` of type `datetime`.
     */
    @Nonnull
    public String getApplyTime() {
        return Objects.requireNonNull(readDateTime("apply_time"));
    }

    /*
     * Field `start_time` of type `datetime`.
     */
    @Nullable
    public String getStartTime() {
        return readDateTime("start_time");
    }

    /*
     * Field `end_time` of type `datetime`.
     */
    @Nullable
    public String getEndTime() {
        return readDateTime("end_time");
    }

    /*
     * Field `feedback` of type `mediumtext`.
     */
    @Nullable
    public String getFeedback() {
        return readString("feedback");
    }

    /**
     * Enum for Field `task_status`
     */
    public enum TaskStatusEnum {
        PENDING,
        RUNNING,
        DONE,
        FAILED,
    }

    /*
     * Enum{PENDING,RUNNING,DONE,FAILED}
     *
     * Loose Enum of Field `task_status` of type `varchar(64)`.
     */
    @Nullable
    public TaskStatusEnum getTaskStatus() {
        @Nullable String enumExpression=readString("task_status");
        if (enumExpression==null) return null;
        return TaskStatusEnum.valueOf(enumExpression);
    }


}

/*
CREATE TABLE `eureka_amphibious_task` (
  `task_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `handler_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_meta` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `apply_time` datetime NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `feedback` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `task_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'PENDING' COMMENT 'Enum{PENDING,RUNNING,DONE,FAILED}',
  PRIMARY KEY (`task_id`),
  KEY `IDX_QUEUE_TASK_SEEKER` (`task_status`,`apply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
