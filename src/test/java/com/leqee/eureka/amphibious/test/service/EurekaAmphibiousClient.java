package com.leqee.eureka.amphibious.test.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class EurekaAmphibiousClient {
    private final String passkey;
    public EurekaAmphibiousClient(String passkey){
        this.passkey = passkey;

    }

    protected Future<JsonObject> call(String api,JsonObject body){
        return Keel.useWebClient(webClient -> webClient
                .postAbs(Keel.config("service.endpoint")+api)
                .sendJsonObject(body.put("passkey",passkey))
        )
                .compose(bufferHttpResponse -> {
                    if(bufferHttpResponse.statusCode()!=200){
                        throw new RuntimeException("Response code "+bufferHttpResponse.statusCode()+": "+bufferHttpResponse.bodyAsString());
                    }
                    JsonObject entries = bufferHttpResponse.bodyAsJsonObject();
                    String code = entries.getString("code");
                    if(Objects.equals("OK",code)) {
                        return Future.succeededFuture(entries);
                    }else{
                        return Future.failedFuture("FAILED: "+entries.getString("data"));
                    }
                });
    }

    public Future<Long> createTask(JsonObject task_meta){
        return this.call("/amphibious/create-task",new JsonObject().put("task_meta",task_meta))
                .compose(resp->{
                    Long task_id = resp.getJsonObject("data").getLong("task_id");
                    return Future.succeededFuture(task_id);
                });
    }
    public Future<JsonObject> queryTask(long task_id){
        return this.call("/amphibious/query-task",new JsonObject().put("task_id",task_id))
                .compose(resp->{
                    var task = resp.getJsonObject("data").getJsonObject("task");
                    return Future.succeededFuture(task);
                });
    }
}
