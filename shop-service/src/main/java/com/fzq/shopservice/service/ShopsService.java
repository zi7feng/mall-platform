package com.fzq.shopservice.service;

import com.fzq.shopservice.model.domain.Shops;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zfeng
* @description ShopsService
* @createDate 2024-03-21 16:28:40
*/
public interface ShopsService extends IService<Shops> {

    long shopRegister(String shopName, String avatarUrl, String description);


}
