package com.fzq.shopservice.controller;


import com.fzq.shopservice.client.userservice.UserServiceClient;
import com.fzq.shopservice.common.BaseResponse;
import com.fzq.shopservice.common.ErrorCode;
import com.fzq.shopservice.common.ResultUtils;
import com.fzq.shopservice.exception.BusinessException;
import com.fzq.shopservice.model.domain.Shops;
import com.fzq.shopservice.model.domain.request.ShopRegisterRequest;
import com.fzq.shopservice.model.domain.userservice.User;
import com.fzq.shopservice.model.domain.userservice.request.UserLoginRequest;
import com.fzq.shopservice.service.ShopsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shops")
@Slf4j
public class ShopsController {

    @Resource
    private ShopsService shopsService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/register")
    // TODO: Create Shop register Request, Complete the method, test.
    public BaseResponse<Long> shopRegister(@RequestBody ShopRegisterRequest shopRegisterRequest) {
        if (shopRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request body should not be null");
        }
        String shopName = shopRegisterRequest.getShopName();
        String shopAvatar = shopRegisterRequest.getAvatarUrl();
        String shopDescription = shopRegisterRequest.getDescription();
        if (StringUtils.isAnyBlank(shopName, shopAvatar, shopDescription)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Params should not be null");
        }
        long result = shopsService.shopRegister(shopName, shopAvatar, shopDescription);
        return ResultUtils.success(result);
    }


    @PostMapping("/shop/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest) {
        return userServiceClient.login(userLoginRequest);
    }
}
