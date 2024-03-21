package com.fzq.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzq.userservice.model.domain.User;
import com.fzq.userservice.model.domain.request.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param username 用户名
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @param phone 手机号
     * @param email 邮箱
     * @return 新用户id
     */
    long userRegister(String userAccount, String username, String userPassword, String checkPassword, String phone, String email);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request 请求
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 脱敏
     * @param originUser 原始用户
     * @return 脱敏用户
     */
    User getSafetyUser(User originUser);

    /**
     * 用户登出
     * @param request 请求
     * @return 登出结果
     */
    int userLogout(HttpServletRequest request);

    /**
     * 获取当前用户
     * @param request 请求
     * @return 当前用户
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 更新用户信息
     * @param userUpdateRequest 用户更新请求
     * @param currentUser 当前用户
     * @return 更新结果
     */
    int updateUser(UserUpdateRequest userUpdateRequest, User currentUser);

    boolean isAdmin(User user);

}