package com.leqee.eureka.amphibious.test;

import com.leqee.eureka.amphibious.navy.mysql.UnionMySQLConnection;
import io.github.sinri.keel.tesuto.TestUnit;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MySQLTableClassGen extends io.github.sinri.drydock.naval.raider.ClassFileGeneratorForMySQLTables {
    /**
     * @return MySQL表对应类所在根对应包的Namespace，没有最后的点。
     */
    @Override
    protected String getTablePackage() {
        return "com.leqee.eureka.amphibious.daemon.table";
    }

    @Nullable
    @Override
    public String getStrictEnumPackage() {
        return null;
    }

    @Nullable
    @Override
    public String getEnvelopePackage() {
        return null;
    }

    /**
     * 本地配置已加载。
     * 准备数据库连接之类的东西。
     *
     * @since 1.2.0
     */
    @NotNull
    @Override
    protected Future<Void> prepareEnvironment() {
        return Future.succeededFuture();
    }

    @TestUnit
    public Future<Void> forUnionKumori() {
        return this.rebuildTablesInSchema(
                "union",
                UnionMySQLConnection::new,
                "kumori",
                List.of(
                        "eureka_amphibious_task",
                        "eureka_amphibious_handler_passkey"
                ));
    }
}
