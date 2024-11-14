package com.leqee.eureka.amphibious.service;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.navy.Main;
import com.leqee.eureka.amphibious.navy.fighter.amphibious.EurekaAmphibiousService;
import io.github.sinri.keel.web.http.AbstractRequestBody;
import io.github.sinri.keel.web.http.ApiMeta;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import javax.annotation.Nonnull;
import java.util.Objects;

@ApiMeta(routePath = "/amphibious/create-task", timeout = 10_000L)
public class EurekaAmphibiousTaskCreateReceptionist extends EurekaAmphibiousService {
    public EurekaAmphibiousTaskCreateReceptionist(RoutingContext routingContext) {
        super(routingContext);
    }

    @Override
    protected Future<Object> handleForFuture() {
        String handlerCode = this.readHandlerCode();
        RequestBody requestBody = new RequestBody(getRoutingContext());

        return Main.getUnionMySQLDataSource().withTransaction(unionMySQLConnection -> {
                    return new EurekaAmphibiousAction(unionMySQLConnection)
                            .createEurekaAmphibiousTask(handlerCode, requestBody.getTaskMeta());
                })
                .compose(task_id -> {
                    return Future.succeededFuture(new JsonObject().put("task_id", task_id));
                });
    }

    private static class RequestBody extends AbstractRequestBody {

        public RequestBody(RoutingContext routingContext) {
            super(routingContext);
        }

        @Nonnull
        public JsonObject getTaskMeta() {
            var x = readJsonObject("task_meta");
            Objects.requireNonNull(x, "task_meta is null");
            return x;
        }
    }
}
