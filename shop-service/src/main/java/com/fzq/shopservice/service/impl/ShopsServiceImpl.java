package com.fzq.shopservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fzq.shopservice.common.ErrorCode;
import com.fzq.shopservice.exception.BusinessException;
import com.fzq.shopservice.model.domain.Shops;
import com.fzq.shopservice.service.ShopsService;
import com.fzq.shopservice.mapper.ShopsMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author zfeng
* @description ShopsServiceImpl
* @createDate 2024-03-21 16:28:40
*/
@Service
@Slf4j
public class ShopsServiceImpl extends ServiceImpl<ShopsMapper, Shops> implements ShopsService {

    @Resource
    private ShopsMapper shopsMapper;

    @Override
    public long shopRegister(String shopName, String avatarUrl, String description) {
        if (StringUtils.isAnyBlank(shopName, avatarUrl, description)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Params cannot be empty");
        }
        if (shopName.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the store name cannot be less than 4");
        }
        if (description.length() < 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The length of the store description cannot be less than 10");
        }
        // no special characters
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(shopName);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The store name cannot contain special characters");
        }
        QueryWrapper<Shops> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shopName", shopName);
        long count = shopsMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Duplicate store name");
        }
        Shops shop = new Shops();
        shop.setShopName(shopName);
        shop.setAvatarUrl(avatarUrl);
        shop.setDescription(description);
        boolean saveResult = this.save(shop);
        if (saveResult) {
            return shop.getId();
        } else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Store registration failed");
        }

    }
}




