package com.charlie.ai.domain.agent.service.armory.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.charlie.ai.domain.agent.model.entity.ArmoryCommandEntity;
import com.charlie.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import com.charlie.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import com.charlie.ai.domain.agent.service.armory.node.RootNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 默认的装配工厂
 * @author: Charlie
 * @date: 2026/10/14 9:40
 */
@Service
public class DefaultArmoryFactory {

    @Resource
    private RootNode rootNode;

    public StrategyHandler<ArmoryCommandEntity, DynamicContext, AiAgentRegisterVO> armoryStrategyHandler(){
        return rootNode;
    }

    /**
     * 定义一个上下文对象，用于各个节点串联的时候，写入数据和使用数据
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private OpenAiApi  openAiApi;

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }

}
