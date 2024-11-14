package com.leqee.eureka.amphibious.service;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.navy.Main;
import com.leqee.eureka.amphibious.navy.fighter.amphibious.EurekaAmphibiousService;
import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousHandlerPasskeyTableRow;
import io.github.sinri.keel.web.http.AbstractRequestBody;
import io.github.sinri.keel.web.http.ApiMeta;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.Nonnull;
import java.util.Objects;

@ApiMeta(routePath = "/amphibious/update-task-handler-passkey", timeout = 10_000L)
public class EurekaAmphibiousTaskHandlerPasskeyUpdateReceptionist extends EurekaAmphibiousService {
    public EurekaAmphibiousTaskHandlerPasskeyUpdateReceptionist(RoutingContext routingContext) {
        super(routingContext);
    }

    @Override
    protected Future<Object> handleForFuture() {
        RequestBody requestBody = new RequestBody(getRoutingContext());

        return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
            String handlerCode = requestBody.getHandlerCode();
            EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum passkeyStatus = requestBody.getPasskeyStatus();
            String passkey = requestBody.getPasskey();
            String remark = requestBody.getRemark();

            EurekaAmphibiousAction kumoriAction = new EurekaAmphibiousAction(unionMySQLConnection);
            return kumoriAction.queryEurekaAmphibiousHandlerPasskeyRecord(passkey)
                    .compose(row -> {
                        if (row == null) {
                            // not existed
                            if (passkeyStatus == EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum.REVOKED) {
                                return Future.failedFuture("REVOKED PASSKEY SHOULD BE NOT UPDATED");
                            } else {
                                return kumoriAction.createEurekaAmphibiousHandlerPasskeyRecord(
                                                handlerCode, passkey, remark
                                        )
                                        .compose(id -> {
                                            return Future.succeededFuture();
                                        });
                            }
                        } else {
                            // existed
                            return kumoriAction.updateEurekaAmphibiousHandlerPasskeyRecord(
                                            passkey, passkeyStatus, remark
                                    )
                                    .compose(afx -> {
                                        return Future.succeededFuture();
                                    });
                        }
                    });
        });
    }

    private static class RequestBody extends AbstractRequestBody {

        public RequestBody(RoutingContext routingContext) {
            super(routingContext);
        }

        @Nonnull
        public String getHandlerCode() {
            return Objects.requireNonNull(readString("handler_code"));
        }

        @Nonnull
        public String getPasskey() {
            return Objects.requireNonNull(readString("passkey"));
        }

        @Nonnull
        public String getRemark() {
            return Objects.requireNonNull(readString("remark"));
        }

        @Nonnull
        public EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum getPasskeyStatus() {
            var x = readString("passkey_status");
            Objects.requireNonNull(x);
            return EurekaAmphibiousHandlerPasskeyTableRow.PasskeyStatusEnum.valueOf(x);
        }
    }
}
