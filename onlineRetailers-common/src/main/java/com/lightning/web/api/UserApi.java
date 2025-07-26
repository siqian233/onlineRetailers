package com.lightning.web.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user")
public interface UserApi  {
}
