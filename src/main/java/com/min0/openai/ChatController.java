package com.min0.openai;

import com.min0.openai.advisor.TokenUsageAuditAdvisor;
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

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        /*
        how to call LLM model. explain about .prompt(msg): to hold prompt message from the end user,
        .call(): to invoke call to LLM,  .content(): to retrieve response from llm.
         */
        return chatClient
                .prompt() // we can send prompt() with empty param and then specify type of message following like system/user/assistant/function
//                .advisors(new TokenUsageAuditAdvisor()) // configure advisor in the controller method
                //.user(message) if I don't pass .user(), the defaultUser() above with the default msg will be sent.
                .call().content();
    }

    @GetMapping("/IT/chat")
    public String ITchat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                // we can override the default system role like below
                .system("""
                        Yuo are an internal IT helpdesk assistant. Your role is to assist employees with IT-related issues such as \s
                        resetting password, unlocking accounts, and answering questions related to IT policies.
                        If a user requests help with anything outside of these responsibilities, respond politely and inform them that \s
                        you are only able to assist with IT support tasks within your defined scope.
                        """)
                .user(message)
                .call().content();
    }
}
