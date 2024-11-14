package com.leqee.eureka.amphibious.action;

import com.leqee.eureka.amphibious.navy.mysql.UnionMySQLConnection;
import io.github.sinri.keel.mysql.action.AbstractNamedMixinAction;
import org.jetbrains.annotations.NotNull;

public class EurekaAmphibiousAction
        extends AbstractNamedMixinAction<UnionMySQLConnection, EurekaAmphibiousAction>
        implements EurekaAmphibiousActionTaskMixin, EurekaAmphibiousActionPasskeyMixin {
    public EurekaAmphibiousAction(@NotNull UnionMySQLConnection namedSqlConnection) {
        super(namedSqlConnection);
    }

    @Override
    public @NotNull EurekaAmphibiousAction getImplementation() {
        return this;
    }
}
