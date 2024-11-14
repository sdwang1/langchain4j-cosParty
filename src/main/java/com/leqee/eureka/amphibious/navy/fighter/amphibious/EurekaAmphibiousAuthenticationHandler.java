package com.leqee.eureka.amphibious.navy.fighter.amphibious;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.navy.Main;
import io.github.sinri.keel.web.http.prehandler.KeelAuthenticationHandler;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class EurekaAmphibiousAuthenticationHandler extends KeelAuthenticationHandler {
    private static final EurekaAmphibiousAuthenticationHandler instance = new EurekaAmphibiousAuthenticationHandler();

    private EurekaAmphibiousAuthenticationHandler() {
        super();
    }

    public static EurekaAmphibiousAuthenticationHandler getInstance() {
        return instance;
    }

    @Override
    protected Future<AuthenticateResult> handleRequest(RoutingContext routingContext) {
        return Future.succeededFuture()
                .compose(v -> {
                    JsonObject jsonObject = routingContext.body().asJsonObject();
                    String passkey = jsonObject.getString("passkey");
                    return Future.succeededFuture(passkey);
                })
                .compose(passkey -> {
                    return Main.getUnionMySQLDataSource().withConnection(unionMySQLConnection -> {
                        return new EurekaAmphibiousAction(unionMySQLConnection).queryValidEurekaAmphibiousHandlerPasskeyRecord(passkey);
                    });
                })
                .compose(row -> {
                    if (row == null) {
                        throw new NullPointerException("PASSKEY IS INVALID");
                    }
                    String handlerCode = row.getHandlerCode();
                    String remark = row.getRemark();
                    return Future.succeededFuture(AuthenticateResult
                            .createAuthenticatedResult(new JsonObject()
                                    .put("handler_code", handlerCode)
                                    .put("remark", remark)
                            ));
                })
                .recover(throwable -> {
                    return Future.succeededFuture(AuthenticateResult
                            .createAuthenticateFailedResult(401, throwable));
                });
    }
}
