package com.leqee.eureka.amphibious.daemon.scenario.verdict;

import com.leqee.eureka.amphibious.daemon.engine.core.AbstractAmphibiousTask;
import com.leqee.eureka.amphibious.daemon.engine.core.ActionRecord;
import com.leqee.eureka.amphibious.daemon.engine.discuss.Discuss;
import com.leqee.eureka.amphibious.table.union.kumori.EurekaAmphibiousTaskTableRow;
import dev.langchain4j.model.chat.ChatLanguageModel;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.List;

public class VerdictTask extends AbstractAmphibiousTask {
    public static final String HandlerCode="VerdictTask";

    public VerdictTask(EurekaAmphibiousTaskTableRow row) {
        super(row);
    }

    @Override
    public Future<String> executeBody() {
        VerdictTaskMeta verdictTaskMeta = new VerdictTaskMeta(getTaskMeta());
        ChatLanguageModel llmModel = verdictTaskMeta.getLLMModel();

        String actorName = "数据事故定责会议主持人";
        String discussIntroduction = """
                # 数据事故定责会议的目的\
                \
                本次数据事故定责会议需要基于下面所述数据事故，\
                从产品经理、大数据开发、数据采集开发、数据源四个不同岗位职责出发分析原因，\
                讨论分析是哪一方因为没有完成职责导致数据事故发生，以此为主要责任方。
                此外，如果有需要负次要责任的也可列出。
                一般地，涉及数据源异常的事故，在没有明确的针对性保障机制的情况下，优先考虑数据源的责任。\
                \
                # 数据事故定责会议需要讨论并定责的数据事故 \
                """ + verdictTaskMeta.getAccidentDescription();

        VerdictHost discussHost = new VerdictHost(llmModel, actorName, discussIntroduction);
        VerdictMember memberForDataSource = new VerdictMember(
                llmModel,
                "数据源维护人员代表",
                "尽可能地从数据源的角度，即外部平台、内部系统、数据维护人员等方面找数据事故原因；" +
                        "例如外部平台页面和结果是否有变动、外部平台有没有阻碍登录或废弃会话的动作、内部系统是否有实现变更、数据维护人员是否维护错数据等。"
        );
        VerdictMember memberForProductManager = new VerdictMember(
                llmModel,
                "产品经理代表",
                "尽可能地从产品经理是否尽责的角度找数据事故原因，优先审视有无需求描述和理解方面的问题；" +
                        "产品经理的职责有与需求方沟通、与数据源侧协调、为开发人员提供正确的逻辑描述、合理配置质检规则等。"
        );
        VerdictMember memberForETL = new VerdictMember(
                llmModel,
                "大数据开发人员代表",
                "尽可能地从大数据开发人员是否尽责的角度找数据事故原因；" +
                        "大数据开发人员的职责有准确实现产品经理给定的数据转化需求、正确配置大数据程序调度并确保集群运转、确保Mizar入库体系正常运作等。" +
                        "一般来说，作为开发人员，仅为基于给定需求的代码实现和配置设定负责；" +
                        "凡需求有问题，上游数据源发生变动或缺失，如数据采集失败、上游系统变更数据格式等，属于大数据开发的能力范围外，不承担责任。"
        );
        VerdictMember memberForFell = new VerdictMember(
                llmModel,
                "数据采集开发人员代表",
                "尽可能地从数据采集开发人员是否尽责的角度找数据事故原因；" +
                        "数据采集开发人员的职责有准确实现产品经理给定的数据采集需求、正确配置采集程序调度、对采集过程中的意外进行响应和尽力维修等。" +
                        "一般来说，作为开发人员，仅为基于给定需求的代码实现和配置设定负责；" +
                        "凡需求有问题，上游数据源发生变动或缺失，如外部平台变更、平台风控导致登录不上会话失效导致数据采集失败等，属于数据采集开发的能力范围外，不承担责任。"
        );
        Discuss discuss = new Discuss(
                discussHost,
                List.of(
                        memberForDataSource,
                        memberForProductManager,
                        memberForETL,
                        memberForFell
                )
        );

        discuss.run();
        List<ActionRecord> actionRecords = discuss.getContext();

        JsonArray array = new JsonArray();
        actionRecords.forEach(actionRecord -> {
            array.add(actionRecord.toJsonObject());
        });
        return Future.succeededFuture(array.toString());
    }
}
