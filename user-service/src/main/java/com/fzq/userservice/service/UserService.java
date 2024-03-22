package com.fzq.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzq.userservice.model.domain.User;
import com.fzq.userservice.model.domain.request.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {
    /**
     * User registration
     * @param userAccount User account
     * @param username User name
     * @param userPassword User password
     * @param checkPassword Check password
     * @param phone Phone number
     * @param email Email
     * @return new user id
     */
    long userRegister(String userAccount, String username, String userPassword, String checkPassword, String phone, String email, Integer userRole);

    /**
     * User login
     * @param userAccount User account
     * @param userPassword User password
     * @param request Request
     * @return desensitized user
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * Get desensitized user
     * @param originUser Original user
     * @return Desensitized user
     */
    User getSafetyUser(User originUser);

    /**
     * User logout
     * @param request Request
     * @return Logout result
     */
    int userLogout(HttpServletRequest request);

    /**
     * Get current user
     * @param request Request
     * @return Current user
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * Update user
     * @param userUpdateRequest User update request
     * @param currentUser Current user
     * @return Update result
     */
    int updateUser(UserUpdateRequest userUpdateRequest, User currentUser);

    boolean isAdmin(User user);

}