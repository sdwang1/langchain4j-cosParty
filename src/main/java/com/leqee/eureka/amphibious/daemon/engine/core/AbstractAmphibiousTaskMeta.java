package com.leqee.eureka.amphibious.daemon.engine.core;

import dev.langchain4j.model.chat.ChatLanguageModel;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAmphibiousTaskMeta {
    protected final @NotNull UnmodifiableJsonifiableEntity unmodifiableJsonifiableEntity;

    public AbstractAmphibiousTaskMeta(@NotNull UnmodifiableJsonifiableEntity unmodifiableJsonifiableEntity) {
        this.unmodifiableJsonifiableEntity = unmodifiableJsonifiableEntity;
    }

    /**
     * @return ChatLanguageModel Using `Qwen` (default) or `OpenAI`
     */
    public ChatLanguageModel getLLMModel() {
        var x = unmodifiableJsonifiableEntity.readString("llm_model");
        if (x == null || x.isBlank()) {
            return  SupportLLModel.DASHSCOPE.buildChatLLM();
        }
        return SupportLLModel.valueOf(x).buildChatLLM();
    }
}
