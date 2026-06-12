package com.min0.openai.config;

import com.min0.openai.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        // create chat options object - we can customize the options
        var options = OpenAiChatOptions.builder().model("gpt-5.4-mini").temperature(0.8).maxCompletionTokens(100);
        return chatClientBuilder
                // set chat options
                .defaultOptions(options)
                // advisor is like a middleware/interceptor for your prompt flow.
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                // let's say I have 10 endpoints that has the same system role, I can use defaultSystem() method to remove redundancy
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help employees with questions related to HR policies,\s
                        such as leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics, kindly inform them that you can only assist\s
                        with queries related to HR policies.
                        """)
                .defaultUser("How can you help me?")
                .build();
    }
}
