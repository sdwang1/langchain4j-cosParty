package com.leqee.eureka.amphibious.service;

import com.leqee.eureka.amphibious.action.EurekaAmphibiousAction;
import com.leqee.eureka.amphibious.navy.Main;
import com.leqee.eureka.amphibious.navy.fighter.amphibious.EurekaAmphibiousService;
import io.github.sinri.keel.web.http.AbstractRequestBody;
import io.github.sinri.keel.web.http.ApiMeta;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Objects;

@ApiMeta(routePath = "/amphibious/query-task", timeout = 10_000L)
public class EurekaAmphibiousTaskQueryReceptionist extends EurekaAmphibiousService {
    public EurekaAmphibiousTaskQueryReceptionist(RoutingContext routingContext) {
        super(routingContext);
    }

    @Override
    protected Future<Object> handleForFuture() {
        RequestBody requestBody = new RequestBody(getRoutingContext());

        String handlerCode = readHandlerCode();

        return Main.getUnionMySQLDataSource().withConnection(unionMySQLConnection -> {
                    return new EurekaAmphibiousAction(unionMySQLConnection)
                            .queryEurekaAmphibiousTask(requestBody.getTaskId());
                })
                .compose(row -> {
                    if (row == null) {
                        return Future.failedFuture("No task found");
                    }
                    if (!Objects.equals(row.getHandlerCode(), handlerCode)) {
                        return Future.failedFuture("No valid task found for handlerCode: " + handlerCode);
                    }
                    return Future.succeededFuture(new JsonObject()
                            .put("task", row.toJsonObject()));
                });
    }

    private static class RequestBody extends AbstractRequestBody {

        public RequestBody(RoutingContext routingContext) {
            super(routingContext);
        }

        public long getTaskId() {
            var x = readLong("task_id");
            Objects.requireNonNull(x, "task_id is null");
            return x;
        }
    }
}
