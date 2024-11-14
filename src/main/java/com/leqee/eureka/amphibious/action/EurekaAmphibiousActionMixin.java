package com.leqee.eureka.amphibious.action;

import com.leqee.eureka.amphibious.navy.mysql.UnionMySQLConnection;
import io.github.sinri.keel.mysql.action.NamedActionMixinInterface;

public interface EurekaAmphibiousActionMixin extends NamedActionMixinInterface<UnionMySQLConnection, EurekaAmphibiousAction> {
}
