package com.lightning.web.controllers;

import com.lightning.web.bean.ResponseResult;
import com.lightning.service.FileDeduplicationService;
import com.lightning.utils.FileTypeValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Log
@RestController
public class FileUploadController {
    @Autowired
    private FileDeduplicationService fileDeduplicationService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult<List<String>> upload(
            @RequestParam("uploadFiles") MultipartFile[] uploadFiles
    ) {
        if (uploadFiles.length == 0) {
            return ResponseResult.error("上传文件不能为空");
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : uploadFiles) {
            if (file.isEmpty()) {
                continue;
            }

            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();  //文件名
            if (!FileTypeValidator.isValidFileType(contentType, originalFilename)) {
                return ResponseResult.error("文件类型不被允许: " + originalFilename + ". 只允许图片、PDF、Word、Excel格式。");
            }

            try {
                //将文件处理和重复数据删除委托给服务
                String fileUrl = fileDeduplicationService.processAndGetFileUrl(file);
                urls.add(fileUrl);
            } catch (Exception e) {
                log.severe("上传文件到OSS失败: " + originalFilename + "\n原因：" + e);
            }
        }
        if (!urls.isEmpty()) {
            log.info("上传到OSS成功,多个图片的访问地址为:" + urls);
            ResponseResult<List<String>> response = ResponseResult.ok("上传接收数据成功");
            return response.setData(urls);
        } else {
            return ResponseResult.error("上传文件失败");
        }
    }
}

