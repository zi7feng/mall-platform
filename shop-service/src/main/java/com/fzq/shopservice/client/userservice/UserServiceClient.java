package com.fzq.shopservice.client.userservice;

import com.fzq.shopservice.common.BaseResponse;
import com.fzq.shopservice.model.domain.userservice.User;
import com.fzq.shopservice.model.domain.userservice.request.UserLoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @PostMapping("/user/login")
    BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest);
}
