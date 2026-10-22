package com.charlie.ai.test.api.tool.mcp;

import com.charlie.ai.test.support.TestEnvironmentHolder;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * @description: Spring Ai Tool
 * @author: Charlie
 * @date: 2026/10/12 8:06
 */
@Slf4j
public class SpringAiToolTest {

    public static void main(String[] args) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api.stepfun.com/")
                .apiKey(TestEnvironmentHolder.getProperty("ai.stepfun.api-key"))
                .completionsPath("v1/chat/completions")
                .embeddingsPath("v1/embeddings")
                .build();

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("step-5-preview")
                        .toolCallbacks(SyncMcpToolCallbackProvider.builder()
                                .mcpClients(sseMcpClient()).build().getToolCallbacks())
                        .build())
                .build();

        String call = chatModel.call("你哪有哪些工具能力");

        log.info("测试结果:{}", call);
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

    @Test
    public void test_url() throws MalformedURLException {
        String fullUrl = "";
//        fullUrl = "http://127.0.0.1:9999/sse?apiKey=xxxx";

        URL url = new URL(fullUrl);

        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();

        String baseUrl = port == -1 ? protocol + "://" + host : protocol + "://" + host + ":" + port;
        String endpoint = "";

        int index = fullUrl.indexOf(baseUrl);
        if (index != -1) {
            endpoint = fullUrl.substring(index + baseUrl.length());
        }

        log.info("baseUrl:{}", baseUrl);
        log.info("endpoint:{}", endpoint);
    }

}
