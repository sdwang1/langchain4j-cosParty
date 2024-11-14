package com.leqee.eureka.amphibious.test.service.discuss;

import com.leqee.eureka.amphibious.test.service.AbstractRoutineTest;
import io.github.sinri.AiOnHttpMix.utils.SupportedModel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class FreeDiscussTest extends AbstractRoutineTest {

    @Override
    protected String passkey() {
        return "TestFreeDiscussTask";
    }

    private JsonObject v1() {
        return new JsonObject("{\"host\":{\"actor_name\":\"主持人\",\"discuss_introduction\":\"讨论是否需要招聘被大厂优化的人。\",\"round\":2,\"conclusion_request\":null},\"members\":[{\"actor_name\":\"大厂仰慕者\",\"contention\":\"能进大厂的人都有两把刷子，现在正是搜捕他们的大好时机！\"},{\"actor_name\":\"大厂恐惧者\",\"contention\":\"能进大厂的人都被大厂文化洗脑过了，他们用的技术和业务也不一定适应我们小作坊，还贵，应该敬而远之。\"}]}");
    }

    @Override
    protected JsonObject taskMeta() {
        return v1();
    }

    protected JsonObject v0() {
        return new JsonObject()
                .put("llm_model", SupportedModel.Volces.name())
                .put("host", new JsonObject()
                        .put("actor_name", "主持人")
                        .put("discuss_introduction",
                                "本次讨论的主题是数据产品经理是否需要了解数据表。\n" +
                                        "数据产品经理一般会在数据分析平台维护数据集并基于数据集为终端用户建立看板，" +
                                        "而数据集的内容和格式是基于数据表生成的，" +
                                        "数据表和其中的数据则是大数据开发人员根据数据产品经理的需求在数据仓库内开发定制的。")
                        .put("round", 3)
                        .put("conclusion_request", "请总结上述不同立场的优劣势，并给出倾向性。")
                )
                .put("members", new JsonArray()
                        .add(new JsonObject()
                                .put("actor_name", "激进派")
                                .put("contention", "数据产品经理不需要了解数据表，只需要管好数据集和看板就行了。")
                        )
                        .add(new JsonObject()
                                .put("actor_name", "保守派")
                                .put("contention", "数据产品经理需要了解数据表，不了解数据表就无法明白数据集的原理。")
                        )
                );
    }
}
