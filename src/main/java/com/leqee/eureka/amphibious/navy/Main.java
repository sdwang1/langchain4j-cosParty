package com.leqee.eureka.amphibious.navy;

import com.leqee.eureka.amphibious.navy.drone.AmphibiousDrone;
import com.leqee.eureka.amphibious.navy.fighter.AmphibiousFighter;
import com.leqee.eureka.amphibious.navy.mysql.UnionMySQLConnection;
import io.github.sinri.drydock.air.Bomber;
import io.github.sinri.drydock.air.Drone;
import io.github.sinri.drydock.air.Fighter;
import io.github.sinri.drydock.naval.carrier.AircraftCarrier;
import io.github.sinri.keel.mysql.KeelMySQLDataSourceProvider;
import io.github.sinri.keel.mysql.NamedMySQLDataSource;
import io.vertx.core.Future;
import io.vertx.core.VertxOptions;
import io.vertx.core.cli.CommandLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public class Main extends AircraftCarrier {
    private static final Main aircraftCarrier=new Main();
    private static NamedMySQLDataSource<UnionMySQLConnection> unionMySQLDataSource;

    public static NamedMySQLDataSource<UnionMySQLConnection> getUnionMySQLDataSource() {
        return unionMySQLDataSource;
    }

    public static void main(String[] args) {
        aircraftCarrier.launch(args);
    }

    public static Main getAircraftCarrier() {
        return aircraftCarrier;
    }

    @Override
    protected Bomber constructBomber() {
        return null;
    }

    @Override
    protected Drone constructDrone() {
        return new AmphibiousDrone(this);
    }

    @Override
    protected Fighter constructFighter(@Nullable Integer port) {
        return new AmphibiousFighter(this, Objects.requireNonNullElse(port,8642));
    }

    @Override
    protected VertxOptions buildVertxOptions(@NotNull CommandLine commandLine) {
        return new VertxOptions();
    }

    @Override
    protected Future<Void> loadRemoteConfiguration(@NotNull CommandLine commandLine) {
        return Future.succeededFuture();
    }

    @Override
    protected @NotNull Future<Void> prepare(@NotNull CommandLine commandLine) {
        if(unionMySQLDataSource==null){
            unionMySQLDataSource= KeelMySQLDataSourceProvider.initializeNamedMySQLDataSource(
                    "union",UnionMySQLConnection::new
            );
        }
        // before Keel 3.2.19, make sure webClient in main verticle to avoid closed by task verticle.
        Keel.useWebClient(webClient -> {
            return Future.succeededFuture();
        });
        return Future.succeededFuture();
    }

    @Override
    protected @NotNull Future<Void> ready(@NotNull CommandLine commandLine) {
        return Future.succeededFuture();
    }

    @Override
    protected @NotNull String buildCliName() {
        return "Eureka Amphibious Daemon";
    }

    @Override
    protected @NotNull String buildCliDescription() {
        return "Eureka Amphibious Daemon";
    }
}