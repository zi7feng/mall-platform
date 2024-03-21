package com.fzq.userservice.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * User registration request
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8223902751003641169L;
    /**
     * user account
     */
    private String userAccount;

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String userPassword;

    /**
     * check password
     */
    private String checkPassword;

    /**
     * phone number
     */
    private String phone;

    /**
     * email
     */
    private String email;

}
