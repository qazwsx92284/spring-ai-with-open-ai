package com.min0.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    // with the help of this chat client we should be able to call the chat api and get the response
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        /*
        how to call LLM model. explain about .prompt(msg): to hold prompt message from the end user,
        .call(): to invoke call to LLM,  .content(): to retrieve response from llm.
         */
        return chatClient.prompt(message).call().content();
    }
}
