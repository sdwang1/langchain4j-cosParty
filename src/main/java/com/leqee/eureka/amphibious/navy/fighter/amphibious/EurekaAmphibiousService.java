package com.leqee.eureka.amphibious.navy.fighter.amphibious;

import com.leqee.eureka.amphibious.navy.Main;
import io.github.sinri.keel.logger.issue.center.KeelIssueRecordCenter;
import io.github.sinri.keel.web.http.PreHandlerChainMeta;
import io.github.sinri.keel.web.http.receptionist.KeelWebFutureReceptionist;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

@PreHandlerChainMeta(EurekaAmphibiousPHC.class)
public abstract class EurekaAmphibiousService extends KeelWebFutureReceptionist {
    public EurekaAmphibiousService(RoutingContext routingContext) {
        super(routingContext);
    }

    protected String readHandlerCode() {
        return getRoutingContext().user().get("handler_code");
    }

    @Override
    protected @NotNull KeelIssueRecordCenter issueRecordCenter() {
        return Main.getAircraftCarrier().getIssueRecordCenter();
    }
}
