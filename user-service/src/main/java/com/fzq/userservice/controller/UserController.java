package com.fzq.userservice.controller;

import com.fzq.userservice.common.BaseResponse;
import com.fzq.userservice.common.ErrorCode;
import com.fzq.userservice.common.ResultUtils;
import com.fzq.userservice.exception.BusinessException;
import com.fzq.userservice.model.domain.User;
import com.fzq.userservice.model.domain.request.UserLoginRequest;
import com.fzq.userservice.model.domain.request.UserRegisterRequest;
import com.fzq.userservice.model.domain.request.UserUpdateRequest;
import com.fzq.userservice.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * user register
     *
     * @param userRegisterRequest userRegisterRequest
     * @return BaseResponse
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request body should not be null");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String username = userRegisterRequest.getUsername();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String phone = userRegisterRequest.getPhone();
        String email = userRegisterRequest.getEmail();
        Integer userRole = userRegisterRequest.getUserRole();
        if (StringUtils.isAnyBlank(userAccount, username, userPassword, checkPassword, phone, email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Params should not be null");
        }
        long result = userService.userRegister(userAccount, username, userPassword, checkPassword, phone, email, userRole);
        return ResultUtils.success(result);
    }

    /**
     * user login
     *
     * @param userLoginRequest userLoginRequest
     * @param request          request
     * @return BaseResponse
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request body should not be null");
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request should not be null");
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        // try to get user from Redis
//        User cachedUser = (User) redisTemplate.opsForValue().get(userAccount);
//        if (cachedUser != null) {
//            log.info("user login from redis: {}", cachedUser);
//            return ResultUtils.success(cachedUser);
//        } else {
//            log.info("user login from database");
//        }
//        User user = userService.userLogin(userAccount, userPassword, request);
//        if (user != null) {
//            // put user cache into Redis
//            redisTemplate.opsForValue().set(userAccount, user, 3600, TimeUnit.SECONDS);
//        }
//        return ResultUtils.success(user);
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * user logout
     *
     * @param request request
     * @return BaseResponse
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request should not be null");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * update user
     * @param userUpdateRequest userUpdateRequest
     * @param request request
     * @return BaseResponse
     */
    @PatchMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Request should not be null");
        }
        User currentUser = userService.getCurrentUser(request);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.updateUser(userUpdateRequest, currentUser);
        return ResultUtils.success(result);
    }

}
