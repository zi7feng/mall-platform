package com.fzq.shopservice.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopRegisterRequest implements Serializable {

    private static final long serialVersionUID = -593528581077156489L;
    /**
     * Shop name
     */
    private String shopName;

    /**
     * Shop avatar
     */
    private String avatarUrl;

    /**
     * Shop description
     */
    private String description;
}
