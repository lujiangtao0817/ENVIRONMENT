package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 环保政策实体
 */
@Data
@TableName("environmental_policy")
public class EnvironmentalPolicy {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String content;
    private Integer policyType;
    private Date publishDate;
    private String source;
    private Long publisherId;
    private Integer viewCount;
    private Integer status;
    private Date createTime;
}
