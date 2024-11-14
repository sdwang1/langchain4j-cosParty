package com.leqee.eureka.amphibious.daemon.scenario.verdict;

import com.leqee.eureka.amphibious.daemon.engine.core.AbstractAmphibiousTaskMeta;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import org.jetbrains.annotations.NotNull;

class VerdictTaskMeta extends AbstractAmphibiousTaskMeta {

    public VerdictTaskMeta(@NotNull UnmodifiableJsonifiableEntity unmodifiableJsonifiableEntity) {
        super(unmodifiableJsonifiableEntity);
    }

    public String getAccidentDescription() {
        return unmodifiableJsonifiableEntity.readString("accident_description");
    }
}
