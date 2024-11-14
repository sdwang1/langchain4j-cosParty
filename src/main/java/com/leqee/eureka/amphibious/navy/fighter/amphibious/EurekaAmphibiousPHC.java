package com.leqee.eureka.amphibious.navy.fighter.amphibious;


import io.github.sinri.keel.web.http.prehandler.PreHandlerChain;

public class EurekaAmphibiousPHC extends PreHandlerChain {
    public EurekaAmphibiousPHC() {
        super();
        this.authenticationHandlers.add(EurekaAmphibiousAuthenticationHandler.getInstance());
    }
}
