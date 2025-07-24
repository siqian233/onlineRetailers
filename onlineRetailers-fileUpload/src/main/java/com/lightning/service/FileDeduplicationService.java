package com.lightning.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.lightning.config.OssConfig;
import com.lightning.utils.FileTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j // 推荐使用 SLF4J
public class FileDeduplicationService {

    private static final String REDIS_IMAGE_MD5_PREFIX = "image:md5:";
    private static final String REDIS_LOCK_PREFIX = "lock:md5:";
    private static final long REDIS_KEY_TTL_DAYS = 30;
    private static final long LOCK_TIMEOUT_SECONDS = 10; // 锁的超时时间，防止死锁

    @Autowired
    private OSS ossClient;

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 处理并获取文件URL，增加了对高并发场景的处理。
     *
     * @param file 上传的文件
     * @return 文件的访问URL
     * @throws IOException      处理文件时发生IO错误
     * @throws InterruptedException 在等待分布式锁时线程被中断
     */
    public String processAndGetFileUrl(MultipartFile file) throws IOException, InterruptedException {
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (!FileTypeValidator.isValidFileType(contentType, originalFilename)) {
            throw new IllegalArgumentException("Invalid file type: " + originalFilename);
        }

        // 1. 一次性读取文件到字节数组，避免多次读取输入流
        byte[] fileBytes = file.getBytes();
        String md5Hash = DigestUtils.md5Hex(fileBytes);
        String redisKey = REDIS_IMAGE_MD5_PREFIX + md5Hash;

        // 2. 第一次检查：文件是否已存在
        String existingFileUrl = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.hasText(existingFileUrl)) {
            log.info("检测到重复文件(MD5：{}),返回现有URL", md5Hash);
            // 刷新过期时间
            stringRedisTemplate.expire(redisKey, REDIS_KEY_TTL_DAYS, TimeUnit.DAYS);
            return existingFileUrl;
        }

        // 3. 设置分布式锁，防止并发上传
        String lockKey = REDIS_LOCK_PREFIX + md5Hash;
        Boolean lockAcquired = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            // 未获取到锁，说明另一个线程正在上传。我们进行短暂轮询等待。
            log.warn("无法获取MD5的锁：{}. 另一个线程可能正在上传", md5Hash);
            // 简单的轮询等待机制
            for (int i = 0; i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);
                existingFileUrl = stringRedisTemplate.opsForValue().get(redisKey);
                if (StringUtils.hasText(existingFileUrl)) {
                    log.info("等待MD5后找到的文件URL: {}", md5Hash);
                    return existingFileUrl;
                }
            }
            // 等待超时后仍未找到，抛出异常或返回错误提示
            throw new IOException("等待超时，由于MD5的上传争用，无法处理文件: " + md5Hash);
        }

        try {
            // 4. 获取到锁后，再次检查文件是否已被上传（双重检查）
            // 这是为了防止在获取锁之前，前一个线程刚好上传完毕并释放了锁。
            existingFileUrl = stringRedisTemplate.opsForValue().get(redisKey);
            if (StringUtils.hasText(existingFileUrl)) {
                log.info("获取锁后检测到重复文件 (MD5: {}).", md5Hash);
                return existingFileUrl;
            }

            // 5. 执行上传逻辑
            log.info("新的唯一文件(MD5：{}), 上传到OSS", md5Hash);
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectKey = "files/" + md5Hash + fileExtension;

            // 使用字节数组创建新的输入流进行上传
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossConfig.getBucketName(),
                    objectKey,
                    new ByteArrayInputStream(fileBytes)
            );
            ossClient.putObject(putObjectRequest);

            String newFileUrl = ossConfig.getUrlPrefix() + "/" + objectKey;

            // 存储到Redis
            stringRedisTemplate.opsForValue().set(redisKey, newFileUrl, REDIS_KEY_TTL_DAYS, TimeUnit.DAYS);
            log.info("上传到OSS的文件：{}. MD5存储在Redis中.", newFileUrl);

            return newFileUrl;
        } finally {
            // 6. 释放锁
            stringRedisTemplate.delete(lockKey);
            log.debug("释放md5锁: {}", md5Hash);
        }
    }
}