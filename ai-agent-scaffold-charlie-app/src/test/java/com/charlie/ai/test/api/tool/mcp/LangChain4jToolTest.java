package com.charlie.ai.test.api.tool.mcp;

import com.charlie.ai.test.support.TestEnvironmentHolder;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * @description: LangChain4j
 * @author: Charlie
 * @date: 2026/10/12 8:26
 */
@Slf4j
public class LangChain4jToolTest {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.stepfun.com/step_plan/v1")
                .apiKey(TestEnvironmentHolder.getProperty("ai.stepfun.api-key"))
                .modelName("step-5-preview")
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .tools(sseMcpClient())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String answer = assistant.chat("你哪有哪些工具能力");
        log.info("测试结果:{}", answer);

    }

    /**
     * 百度搜索MCP服务；https://console.bce.baidu.com/iam/?_=1753597622044#/iam/apikey/list
     */
    public static McpSyncClient sseMcpClient() {

        // 自己申请 api_key
        HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport.
                builder("http://appbuilder.baidu.com/v2/ai_search/mcp/")
                .sseEndpoint("sse?api_key=" + TestEnvironmentHolder.getProperty("ai.baidu.mcp.api-key"))
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMinutes(360)).build();
        var init_sse = mcpSyncClient.initialize();
        log.info("Tool SSE MCP Initialized {}", init_sse);

        return mcpSyncClient;
    }
}
