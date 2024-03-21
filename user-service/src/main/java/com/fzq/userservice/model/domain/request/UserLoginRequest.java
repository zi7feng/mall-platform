package com.fzq.userservice.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User login request
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3415754258365948002L;
    /**
     * User account
     */
    private String userAccount;

    /**
     * password
     */
    private String userPassword;
}
