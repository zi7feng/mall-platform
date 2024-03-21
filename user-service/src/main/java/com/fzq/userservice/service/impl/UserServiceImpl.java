package com.fzq.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzq.userservice.common.ErrorCode;
import com.fzq.userservice.exception.BusinessException;
import com.fzq.userservice.mapper.UserMapper;
import com.fzq.userservice.model.domain.User;
import com.fzq.userservice.model.domain.request.UserUpdateRequest;
import com.fzq.userservice.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.fzq.userservice.constant.UserConstant.ADMIN_ROLE;
import static com.fzq.userservice.constant.UserConstant.USER_LOGIN_STATE;

/**
 * user service
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * salt
     */
    private static final String SALT = "fzqqqqq";


    @Resource
    private UserMapper userMapper;


    /**
     * register
     * @param userAccount userAccount
     * @param username username
     * @param userPassword password
     * @param checkPassword check password
     * @param phone phone
     * @param email email
     * @return newUser id
     */
    @Override
    public long userRegister(String userAccount, String username, String userPassword, String checkPassword, String phone, String email) {
        if (StringUtils.isAnyBlank(userAccount, username, userPassword, checkPassword, phone, email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帐号长度不能小于4");
        }
        if (username.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度不能小于4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于8");
        }
        // no special characters
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "帐号不能包含特殊字符");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        // 用户重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "duplicate account");
        }

        // encrypt
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUsername(username);
        user.setUserPassword(encryptPassword);
        user.setPhone(phone);
        user.setEmail(email);
        boolean saveResult = this.save(user);
        if (saveResult) {
            return user.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "register failed");
        }

    }

    /**
     * login
     * @param userAccount userAccount
     * @param userPassword password
     * @param request request
     * @return User
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度不能小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能小于8");
        }
        // no special characters
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // encrypt
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // is account exist
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // isn't exist
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 3. desensitization
        User safetyUser = getSafetyUser(user);
        // 4. set login state
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * desensitization
     * @param originUser originUser
     * @return User
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setUpdateTime(originUser.getUpdateTime());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setProfile(originUser.getProfile());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setBirthday(originUser.getBirthday());
        safeUser.setCity(originUser.getCity());
        safeUser.setIntegration(originUser.getIntegration());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserLevel(originUser.getUserLevel());
        safeUser.setUsername(originUser.getUsername());
        return safeUser;
    }

    /**
     * logout
     * @param request request
     * @return int
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * get current user
     * @param request request
     * @return User
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return user;
    }

    @Override
    public int updateUser(UserUpdateRequest userUpdateRequest, User currentUser) {
        Long id = userUpdateRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // check authority
        // 2.1 admin can update any info, user can only update their info
        if (!isAdmin(currentUser) && !id.equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        User oldUser = this.getById(userUpdateRequest.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // update
        oldUser.setUsername(userUpdateRequest.getUsername());
        oldUser.setAvatarUrl(userUpdateRequest.getAvatarUrl());
        oldUser.setGender(userUpdateRequest.getGender());
        oldUser.setProfile(userUpdateRequest.getProfile());
        oldUser.setPhone(userUpdateRequest.getPhone());
        oldUser.setEmail(userUpdateRequest.getEmail());
        oldUser.setBirthday(userUpdateRequest.getBirthday());
        oldUser.setCity(userUpdateRequest.getCity());
        return this.baseMapper.updateById(oldUser);

    }

    @Override
    public boolean isAdmin(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return user.getUserRole() == ADMIN_ROLE;
    }
}

