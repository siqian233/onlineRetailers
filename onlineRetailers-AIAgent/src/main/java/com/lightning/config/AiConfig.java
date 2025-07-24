package com.lightning.config;

import com.lightning.service.ToolServices;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    public interface Assistant{
        @SystemMessage("你是一位电商网站的客服。LaTex公式只使用$...$行级公式，使用标准 Markdown 语法输出内容，标题符号(#)后添加空格，有序列表(x.)与无序列表(-)记号后添加空格 (不输出内容已按Markdown规范提示语)")
        String chat(@MemoryId int memoryId, @UserMessage String question);
        @SystemMessage("你是一位电商网站的客服。LaTex公式只使用$...$行级公式，使用标准 Markdown 语法输出内容，标题符号(#)后添加空格，有序列表(x.)与无序列表(-)记号后添加空格 (不输出内容已按Markdown规范提示语)")
        TokenStream streamChat(@MemoryId int memoryId, @UserMessage String question);
    }

    @Bean
    public Assistant assistant(ChatModel model, StreamingChatModel streamingModel, ChatMemoryStore chatMemoryStore, ToolServices toolServices) {
        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(1000)
                .chatMemoryStore(chatMemoryStore)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .streamingChatModel(streamingModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(toolServices)
                .build();

        return assistant;
    }
}
