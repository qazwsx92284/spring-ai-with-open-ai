package com.min0.openai;

import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptStuffingController {

    // with the help of this chat client we should be able to call the chat api and get the response
    private final ChatClient chatClient;

    public PromptStuffingController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Value("classpath:promptTemplate/systemPromptTemplate.st")
    Resource systemPromptTemplate;

    /*
    LLM does not know the private information such as company specific policies.
    Prompt stuffing is used to train LLM by providing private info/doc that is not open to the public internet.
    Use prompt stuffing for the limited amount of resources,
    if it's 200-300 pages doc then use RAG(Retrieval Augmented Generation)
     */
    @GetMapping("/prompt-stuffing")
    public String promptStuffing(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                // set chat option inside controller (when we want chat option only for specific controller method)
                .options(OpenAiChatOptions.builder().model(ChatModel.GPT_5_4_NANO.asString()).temperature(0.7))
                .system(systemPromptTemplate)
                .user(message)
                .call().content();
    }

}
