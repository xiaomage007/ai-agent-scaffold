package com.charlie.ai.domain.agent.service.chat;

import com.charlie.ai.domain.agent.model.entity.ChatCommandEntity;
import com.charlie.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.charlie.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import com.charlie.ai.domain.agent.service.IChatService;
import com.charlie.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import com.google.adk.events.Event;
import io.reactivex.rxjava3.core.Flowable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Charlie
 * @date: 2026/10/14 9:37
 */
public class ChatService implements IChatService {

    @jakarta.annotation.Resource
    private DefaultArmoryFactory defaultArmoryFactory;

    @Resource
    private AiAgentAutoConfigProperties aiAgentAutoConfigProperties;


    @Override
    public List<AiAgentConfigTableVO.Agent> queryAiAgentConfigList() {
        Map<String, AiAgentConfigTableVO> tables = aiAgentAutoConfigProperties.getTables();
        List<AiAgentConfigTableVO.Agent> agentList = new ArrayList<>();
        tables.forEach((agentId, aiAgentConfigTableVO) -> {
            agentList.add(aiAgentConfigTableVO.getAgent());
        });
        return agentList;
    }

    @Override
    public String createSession(String agentId, String userId) {
        return "";
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String message) {
        return List.of();
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String sessionId, String message) {
        return List.of();
    }

    @Override
    public Flowable<Event> handleMessageStream(String agentId, String userId, String sessionId, String message) {
        return null;
    }

    @Override
    public List<String> handleMessage(ChatCommandEntity chatCommandEntity) {
        return List.of();
    }
}
