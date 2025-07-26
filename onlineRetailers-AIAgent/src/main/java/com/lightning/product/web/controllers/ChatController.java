package com.lightning.product.web.controllers;

import com.lightning.product.config.AiConfig;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * 聊天控制器，提供多种AI聊天接口
 */
@RestController
@RequestMapping("/ai")
public class ChatController {
    @Autowired
    private ChatModel chatModel;
    @Autowired
    private StreamingChatModel streamingChatModel;
    @Autowired
    private AiConfig.Assistant assistant;

    /**
     * 同步聊天接口
     *
     * @param userMessage 用户提问内容
     * @return AI的回复内容
     */
    @RequestMapping("/chat")
    public String chat(@RequestParam("question") String userMessage) {
        return chatModel.chat(userMessage);
    }

    /**
     * 流式聊天接口（服务器端打印结果）
     *
     * @param question 用户提问内容
     */
    @RequestMapping("/streamingChat")
    public void streamingChat(@RequestParam("question") String question) {
        streamingChatModel.chat(question, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String s) {
                System.out.print(s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                System.out.println(chatResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }

    /**
     * 基于Flux的流式聊天接口
     *
     * @param question 用户提问内容
     * @return 返回Flux<String>流式响应
     */
    @RequestMapping("/streamingchat2")
    public Flux<String> streamingChat2(@RequestParam("question") String question) {
        Flux<String> response = Flux.create(sink -> {
            streamingChatModel.chat(question, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String s) {
                    sink.next(s);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    sink.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }
            });
        });

        return response;
    }

    /**
     * 带记忆的同步聊天接口
     *
     * @param memoryId 记忆ID，用于关联对话历史
     * @param question 用户提问内容
     * @return AI的回复内容
     */
    @RequestMapping("/assistantchat")
    public String assistantChat(@RequestParam("memoryId") int memoryId, @RequestParam("question") String question) {
        return assistant.chat(memoryId, question);
    }

    /**
     * 带记忆的流式聊天接口
     *
     * @param memoryId 记忆ID，用于关联对话历史
     * @param question 用户提问内容
     * @return 返回Flux<String>流式响应
     */
    @RequestMapping("/assistantstreamingchat")
    public Flux<String> assistantStreamingChat(@RequestParam("memoryId") int memoryId, @RequestParam("question") String question) {
        TokenStream tokenStream = assistant.streamChat(memoryId, question);

        return Flux.create(sink -> {
            tokenStream.onCompleteResponse(response -> sink.complete());
            tokenStream.onError(sink::error);
            tokenStream.onPartialResponse(sink::next);
            tokenStream.start();
        }, FluxSink.OverflowStrategy.BUFFER);
    }

}
