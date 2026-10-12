package com.charlie.ai.test.api.model;

import com.charlie.ai.test.support.TestEnvironmentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

/**
 * @description: Spring AI Test 文档：<a href="https://docs.spring.io/spring-ai/reference/1.0/api/advisors.html">spring ai</a>
 * @author: Charlie
 * @date: 2026/10/10 8:16
 */
@Slf4j
public class SpringAiApiTest {

    public static void main(String[] args) {

        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api.stepfun.com/step_plan")
                .apiKey(TestEnvironmentHolder.getProperty("ai.stepfun.api-key"))
                .build();


        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder().model("step-5-preview").build())
                .build();


        String call = chatModel.call("hi 你好哇!");

        log.info("测试结果:{}", call);

    }

}
