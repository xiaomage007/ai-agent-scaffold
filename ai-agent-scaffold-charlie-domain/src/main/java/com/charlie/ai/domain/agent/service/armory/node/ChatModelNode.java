package com.charlie.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.charlie.ai.domain.agent.model.entity.ArmoryCommandEntity;
import com.charlie.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.charlie.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import com.charlie.ai.domain.agent.service.armory.AbstractArmorySupport;
import com.charlie.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import com.charlie.ai.domain.agent.service.armory.mcp.client.ToolMcpCreateService;
import com.charlie.ai.domain.agent.service.armory.mcp.client.factory.DefaultMcpClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 装配域节点-ChatModelNode
 * @author: Charlie
 * @date: 2026/10/15 8:45
 */
@Slf4j
@Service
public class ChatModelNode extends AbstractArmorySupport {

    @Resource
    private AgentNode agentNode;

    @Resource
    private DefaultMcpClientFactory defaultMcpClientFactory;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity requestParameter, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - ChatModelNode");

        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();

        AiAgentConfigTableVO aiAgentConfigTableVO = requestParameter.getAiAgentConfigTableVO();
        AiAgentConfigTableVO.Module.ChatModel chatModelConfig = aiAgentConfigTableVO.getModule().getChatModel();

        List<AiAgentConfigTableVO.Module.ChatModel.ToolMcp> toolMcpList = chatModelConfig.getToolMcpList();
        List<ToolCallback> toolCallbackList = new ArrayList<>();
        for (AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp : toolMcpList) {
            ToolMcpCreateService tooMcpCreateService = defaultMcpClientFactory.getTooMcpCreateService(toolMcp);
            ToolCallback[] toolCallbacks = tooMcpCreateService.buildToolCallback(toolMcp);
            toolCallbackList.addAll(List.of(toolCallbacks));
        }

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatModelConfig.getModel())
                        .toolCallbacks(toolCallbackList)
                        .build())
                .build();

        dynamicContext.setChatModel(chatModel);

        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentNode;
    }

}
