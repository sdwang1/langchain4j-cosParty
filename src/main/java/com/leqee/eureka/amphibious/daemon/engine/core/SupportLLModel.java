package com.leqee.eureka.amphibious.daemon.engine.core;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.qianfan.QianfanChatModel;

import static io.github.sinri.keel.facade.KeelInstance.Keel;

public enum SupportLLModel {
    OPENAI,
    DASHSCOPE,
    QIANFAN;

    public ChatLanguageModel buildChatLLM() {
        String name = this.name();
        String apiKey = Keel.config(name + ".api_key");
        String modelName = Keel.config(name + ".model_name");
        String maxTokensStr = Keel.config(name + ".max_tokens");
        String temperatureStr = Keel.config(name + ".temperature");
        String secretKey = Keel.config(name + ".secret_key");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException(name + " LLM SECRET KEY NOT SET");
        }
        if (modelName == null || modelName.isBlank()) {
            throw new IllegalArgumentException(name + " LLM MODEL NAME NOT SET");
        }
        Integer maxTokens = null;
        if (maxTokensStr != null && maxTokensStr.matches("^[1-9][0-9]*$")) {
            maxTokens = Integer.parseInt(maxTokensStr);
        }
        double temperature = 0;
        if (temperatureStr != null && temperatureStr.matches("^(0\\.[1-9]\\d*|0\\.[0-9]*[1-9]|1\\.0*|1)$")) {
            temperature = Double.parseDouble(temperatureStr);
        }

        return switch (this) {
            case OPENAI -> OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .build();
            case DASHSCOPE -> QwenChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .maxTokens(maxTokens)
                    .temperature((float) temperature)
                    .build();
            case QIANFAN -> QianfanChatModel.builder()
                    .apiKey(apiKey)
                    .secretKey(secretKey)
                    .modelName(modelName)
                    .maxOutputTokens(maxTokens)
                    .temperature(temperature)
                    .build();
        };
    }
}
