package com.leqee.eureka.amphibious.action;

import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousHandlerPasskeyTableRow;
import io.github.sinri.keel.mysql.statement.AnyStatement;
import io.vertx.core.Future;

public interface EurekaAmphibiousActionPasskeyMixin extends EurekaAmphibiousActionMixin {
    default Future<EurekaAmphibiousHandlerPasskeyTableRow> queryEurekaAmphibiousHandlerPasskeyRecord(String passkey) {
        return AnyStatement.select(selectStatement -> selectStatement
                        .from(EurekaAmphibiousHandlerPasskeyTableRow.SCHEMA_AND_TABLE)
                        .where(conditionsComponent -> conditionsComponent
                                .expressionEqualsLiteralValue("passkey", passkey)
                        )
                        .limit(1)
                )
                .queryForOneRow(getNamedSqlConnection(), EurekaAmphibiousHandlerPasskeyTableRow.class);
    }

    /**
     * @param passkey The passkey, of the handler
     * @return an async result of EurekaAmphibiousHandlerPasskeyTableRow, null for not found.
     */
    default Future<EurekaAmphibiousHandlerPasskeyTableRow> queryValidEurekaAmphibiousHandlerPasskeyRecord(String passkey) {
        return queryEurekaAmphibiousHandlerPasskeyRecord(passkey)
                .compose(row -> {
                    if (row == null) {
                        return Future.succeededFuture(null);
                    }
                    if (row.getPasskeyStatus() != EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum.NORMAL) {
                        return Future.succeededFuture(null);
                    }
                    return Future.succeededFuture(row);
                });
    }

    default Future<Long> createEurekaAmphibiousHandlerPasskeyRecord(
            String handler_code,
            String passkey,
            String remark
    ) {
        return AnyStatement.insert(writeIntoStatement -> writeIntoStatement
                        .intoTable(EurekaAmphibiousHandlerPasskeyTableRow.SCHEMA_AND_TABLE)
                        .macroWriteOneRow(row -> row
                                .put("handler_code", handler_code)
                                .put("passkey", passkey)
                                .put("passkey_status", EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum.NORMAL.name())
                                .putNow("create_time")
                                .put("remark", remark)
                        )
                )
                .executeForLastInsertedID(getNamedSqlConnection());
    }

    default Future<Integer> updateEurekaAmphibiousHandlerPasskeyRecord(
            String passkey,
            EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum status,
            String remark
    ) {
        return AnyStatement.update(updateStatement -> {
                    updateStatement
                            .table(EurekaAmphibiousHandlerPasskeyTableRow.SCHEMA_AND_TABLE)
                            .setWithValue("passkey_status", status.name());
                    if (status == EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum.REVOKED) {
                        updateStatement.setWithExpression("revoke_time", "now()");
                    }
                    updateStatement.setWithExpression("remark", remark);
                    updateStatement.where(conditionsComponent -> conditionsComponent
                            .expressionEqualsLiteralValue("passkey", passkey));
                })
                .executeForAffectedRows(getNamedSqlConnection());
    }
}
