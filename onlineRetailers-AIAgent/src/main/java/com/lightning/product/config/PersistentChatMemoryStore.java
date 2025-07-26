package com.lightning.product.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private static final String REDIS_KEY_PREFIX = "chat:memory:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;   //redis的操作模板类

    public PersistentChatMemoryStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(Object memoryId) {
        return REDIS_KEY_PREFIX + memoryId.toString();
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String key = getKey(memoryId);
        String messageJson = redisTemplate.opsForValue().get(key);
        if (messageJson == null || messageJson.isEmpty()) {
            return List.of();
        }
        return ChatMessageDeserializer.messagesFromJson(messageJson);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String key = getKey(memoryId);
        String json = ChatMessageSerializer.messagesToJson(messages);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        this.redisTemplate.delete(getKey(memoryId));
    }
}
