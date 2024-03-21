package com.fzq.userservice.model.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * log bean
 */
@Document(collection = "logs")
@Data
public class LogBean {
    private String id;
    private Integer userId;
    private String username;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;
    private String ip;
    private String className; //类名
    private String method; //方法名
    private String reqParam; //请求
}
