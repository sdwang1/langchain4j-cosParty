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
 * TABLE: eureka_amphibious_handler_passkey
 * (*￣∇￣*)
 * NOTICE BY KEEL:
 * 	To avoid being rewritten, do not modify this file manually, unless editable confirmed.
 * 	It was auto-generated on Mon Oct 14 11:15:34 CST 2024.
 * @see io.github.sinri.keel.mysql.dev.TableRowClassSourceCodeGenerator
 */
public class EurekaAmphibiousHandlerPasskeyTableRow extends AbstractTableRow {
    public static final String SCHEMA_AND_TABLE = "kumori.eureka_amphibious_handler_passkey";

    public EurekaAmphibiousHandlerPasskeyTableRow(JsonObject tableRow) {
        super(tableRow);
    }

    @Override
    @Nonnull
    public String sourceTableName() {
        return "eureka_amphibious_handler_passkey";
    }

    public String sourceSchemaName() {
        return "kumori";
    }

    /*
     * Field `id` of type `bigint(20) unsigned`.
     */
    @Nonnull
    public Long getId() {
        return Objects.requireNonNull(readLong("id"));
    }

    /*
     * Field `handler_code` of type `varchar(200)`.
     */
    @Nonnull
    public String getHandlerCode() {
        return Objects.requireNonNull(readString("handler_code"));
    }

    /*
     * Field `passkey` of type `varchar(128)`.
     */
    @Nonnull
    public String getPasskey() {
        return Objects.requireNonNull(readString("passkey"));
    }

    /*
     * Field `remark` of type `varchar(128)`.
     */
    @Nonnull
    public String getRemark() {
        return Objects.requireNonNull(readString("remark"));
    }

    /*
     * Enum{NORMAL,REVOKED}
     *
     * Loose Enum of Field `passkey_status` of type `varchar(64)`.
     */
    @Nonnull
    public PasskeyStatusEnum getPasskeyStatus() {
        @Nullable String enumExpression = readString("passkey_status");
        Objects.requireNonNull(enumExpression, "The Enum Field `passkey_status` should not be null!");
        return PasskeyStatusEnum.valueOf(enumExpression);
    }

    /*
     * Field `create_time` of type `datetime`.
     */
    @Nonnull
    public String getCreateTime() {
        return Objects.requireNonNull(readDateTime("create_time"));
    }

    /*
     * Field `revoke_time` of type `datetime`.
     */
    @Nullable
    public String getRevokeTime() {
        return readDateTime("revoke_time");
    }

    /**
     * Enum for Field `passkey_status`
     */
    public enum PasskeyStatusEnum {
        NORMAL,
        REVOKED,
    }


}

/*
CREATE TABLE `eureka_amphibious_handler_passkey` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `handler_code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `passkey` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `passkey_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NORMAL' COMMENT 'Enum{NORMAL,REVOKED}',
  `create_time` datetime NOT NULL,
  `revoke_time` datetime DEFAULT NULL,
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_PASSKEY` (`passkey`),
  UNIQUE KEY `UK_HANDLER_REMARK` (`handler_code`,`remark`),
  KEY `handler_code` (`handler_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
 */
