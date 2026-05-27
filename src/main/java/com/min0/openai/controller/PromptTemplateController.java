package com.min0.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PromptTemplateController {

    private final ChatClient chatClient;

    public PromptTemplateController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    String promptTemplate =
            """
             A customer named {customerName} sent the following message:
             "{customerMessage}"
             
             Write a polite and helpful email response addressing the issue.
             Maintain a professional tone and provide reaaurance.
             
             Respond as if you're writing the email body only. Don't include subject and signature.
            """;

    // Suppose customer support team to get the potential email responses that
    // they can send to the customer based upon the complaint they raise.
    @GetMapping("/email")
    public String emailResponse(@RequestParam("customerName") String custName, @RequestParam("customerMessage")String custMsg) {
        return chatClient
                .prompt()
                .system(
                        """
                             you are a professional customer service assistant which helps drafting email responses to improve the \s
                             productivity of the customer support team.
                            """)
                .user(promptTemplateSpec -> promptTemplateSpec.text(promptTemplate)
                        .param("customerName", custName)
                        .param("customerMessage", custMsg))
                .call().content();
    }
}
