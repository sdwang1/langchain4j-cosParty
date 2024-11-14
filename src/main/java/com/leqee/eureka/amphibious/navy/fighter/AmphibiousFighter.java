package com.leqee.eureka.amphibious.navy.fighter;

import com.leqee.eureka.amphibious.navy.fighter.amphibious.EurekaAmphibiousService;
import io.github.sinri.drydock.air.Fighter;
import io.github.sinri.drydock.naval.carrier.AircraftCarrierDeck;
import io.github.sinri.keel.web.http.fastdocs.KeelFastDocsKit;
import io.github.sinri.keel.web.http.receptionist.KeelWebReceptionistLoader;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class AmphibiousFighter extends Fighter {
    public AmphibiousFighter(@NotNull AircraftCarrierDeck deck, int port) {
        super(deck, port);
    }

    @Override
    public void configureHttpServerRoutes(Router router) {
        KeelFastDocsKit.installToRouter(
                router,
                "/amphibious/docs/",
                "fastdocs",
                "Eureka Amphibious",
                "Copyright 2024 Amphibious Branch of Eureka Group"
        );
        KeelWebReceptionistLoader.loadPackage(
                router,
                "com.leqee.eureka.amphibious.service",
                EurekaAmphibiousService.class
        );
    }

    @Override
    public @NotNull Future<Void> beforeStartHttpServer() {
        return Future.succeededFuture();
    }
}
