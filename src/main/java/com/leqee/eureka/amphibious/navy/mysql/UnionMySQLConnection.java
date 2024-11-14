package com.leqee.eureka.amphibious.navy.mysql;

import io.github.sinri.keel.mysql.NamedMySQLConnection;
import io.vertx.sqlclient.SqlConnection;
import org.jetbrains.annotations.NotNull;

public class UnionMySQLConnection extends NamedMySQLConnection {
    public static final String DataSourceName = "union";

    public UnionMySQLConnection(SqlConnection sqlConnection) {
        super(sqlConnection);
    }

    @NotNull
    @Override
    public String getDataSourceName() {
        return DataSourceName;
    }
}
