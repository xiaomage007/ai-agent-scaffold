package com.charlie.ai.test.support;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @description: 测试配置加载；从 application-dev.yml 读取配置，密钥经环境变量注入不落库
 * @author: Charlie
 * @date: 2026/10/12
 */
public final class TestEnvironmentHolder {

    private static final Environment ENVIRONMENT = load();

    private TestEnvironmentHolder() {
    }

    public static String getProperty(String key) {
        return ENVIRONMENT.getProperty(key);
    }

    private static Environment load() {
        StandardEnvironment environment = new StandardEnvironment();
        try {
            new YamlPropertySourceLoader()
                    .load("application-dev", new ClassPathResource("application-dev.yml"))
                    .forEach(environment.getPropertySources()::addLast);
        } catch (IOException e) {
            throw new IllegalStateException("加载 application-dev.yml 失败", e);
        }
        return environment;
    }
}
