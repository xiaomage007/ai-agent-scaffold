package com.charlie.ai.test.api.model;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: LangChain4j
 * 文档：<a href="https://docs.langchain4j.info/">langchain4j</a>
 * 案例：<a href="https://github.com/langchain4j/langchain4j-examples">langchain4j-examples</a>
 * @author: Charlie
 * @date: 2026/10/12 7:39
 */
@Slf4j
public class LangChain4jApiTest {

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.stepfun.com/step_plan/v1")
                .apiKey("134ajznaQWAd0bhZ5bcnOQZGzR6jl8EKNOYHsCxJkvszVFCaG8AqmXZlnW5JNAyxI")
                .modelName("step-5-preview")
                .build();

        String chat = model.chat("hi 你好哇!");
        log.info("测试结果:{}", chat);
    }

}
