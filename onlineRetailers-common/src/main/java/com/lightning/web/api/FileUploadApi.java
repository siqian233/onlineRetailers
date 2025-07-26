package com.lightning.web.api;

import com.lightning.config.FeignMultipartSupportConfig;
import com.lightning.web.bean.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "fileUpload", configuration = FeignMultipartSupportConfig.class) //上传协议需要自开发
public interface FileUploadApi {
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseResult upload(@RequestPart("uploadFiles")
                  MultipartFile[] uploadFiles);
}
