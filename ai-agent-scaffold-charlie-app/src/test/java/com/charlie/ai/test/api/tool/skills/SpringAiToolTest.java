package com.charlie.ai.test.api.tool.skills;

import com.charlie.ai.test.support.TestEnvironmentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;

/**
 * @description:
 * @author: Charlie
 * @date: 2026/10/22 7:58
 */
@Slf4j
public class SpringAiToolTest {

    public static void main(String[] args) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api.nengpa.com")
                .apiKey(TestEnvironmentHolder.getProperty("ai.stepfun.api-key"))
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        ToolCallback toolCallback02 = SkillsTool.builder()
                .addSkillsResource(new ClassPathResource("agent/skills"))
                .build();

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("MiniMax-M3")
                        .toolCallbacks(new ArrayList<>(){{
                            add(toolCallback02);
                        }})
                        .build())
                .build();

        String call = chatModel.call("你哪有哪些工具能力");

        log.info("测试结果:{}", call);
    }
}
