package com.min0.openai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {

    private final ChatClient chatClient;


    public StreamController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message")String message) {
        return chatClient.prompt().user(message)
                /*
                stream() returns object surrounded by Flux because with stream() method LLM sends the response
                as it is generating the response. The streaming process continuously emit the message from the LLM to spring AI app.
                The main advantage of using Flux is that it is not going to block the thread until the entire response is received.
                As and when a response is received the same will be processed and the thread will be free until the next response
                is being received from the LLM. Flux ~= conveyor belt
                 */
                .stream()
                .content();
    }
}
