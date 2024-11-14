package com.leqee.eureka.amphibious.test.service.verdict;

import com.leqee.eureka.amphibious.test.service.AbstractRoutineTest;
import io.vertx.core.json.JsonObject;

public class VerdictTaskMixBlock extends AbstractRoutineTest {

    @Override
    protected String passkey() {
        return "TestVerdictTask";
    }

    @Override
    protected JsonObject taskMeta() {
        return new JsonObject()
//                .put("llm_model", SupportedModel.Volces.name())
                .put("accident_description", "采集集群机器崩溃，导致8小时数据缺失。");
    }
}
