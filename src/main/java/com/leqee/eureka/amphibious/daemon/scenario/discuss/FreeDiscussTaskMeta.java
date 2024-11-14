package com.leqee.eureka.amphibious.daemon.scenario.discuss;

import com.leqee.eureka.amphibious.daemon.engine.core.AbstractAmphibiousTaskMeta;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

class FreeDiscussTaskMeta extends AbstractAmphibiousTaskMeta {


    public FreeDiscussTaskMeta(@NotNull UnmodifiableJsonifiableEntity unmodifiableJsonifiableEntity) {
        super(unmodifiableJsonifiableEntity);
    }


    public HostMeta getHostMeta() {
        return new HostMeta(Objects.requireNonNull(unmodifiableJsonifiableEntity.readJsonObject("host")));
    }

    public List<MemberMeta> getMemberMetaList() {
        List<JsonObject> array = unmodifiableJsonifiableEntity.readJsonObjectArray("members");
        Objects.requireNonNull(array);
        return array.stream().map(MemberMeta::new).toList();
    }

    public static class HostMeta extends UnmodifiableJsonifiableEntityImpl {

        public HostMeta(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getHostName() {
            return readString("actor_name");
        }

        public String getDiscussIntroduction() {
            return readString("discuss_introduction");
        }

        public int getRound() {
            Integer round = readInteger("round");
            return Objects.requireNonNullElse(round, 1);
        }

        public @Nullable String getConclusionRequest() {
            return readString("conclusion_request");
        }
    }

    public static class MemberMeta extends UnmodifiableJsonifiableEntityImpl {

        public MemberMeta(@NotNull JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getActorName() {
            return readString("actor_name");
        }

        public String getContention() {
            return readString("contention");
        }
    }
}
