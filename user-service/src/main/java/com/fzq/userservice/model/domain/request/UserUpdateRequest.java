package com.fzq.userservice.model.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -7895642130678486837L;
    /**
     * id
     */
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * user avatar url
     */
    private String avatarUrl;

    /**
     * gender
     */
    private String gender;

    /**
     * profile
     */
    private String profile;


    /**
     * phone number
     */
    private String phone;

    /**
     * email
     */
    private String email;

    /**
     * birthday
     */
    private Date birthday;

    /**
     * city
     */
    private String city;

}
