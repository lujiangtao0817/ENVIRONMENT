package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统公告实体
 */
@Data
@TableName("announcement")
public class Announcement {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String content;
    private Long publisherId;
    private String publisherName;
    private Integer isTop;
    private Integer status;
    private Date publishTime;
    private Date createTime;
    private Date updateTime;
}
