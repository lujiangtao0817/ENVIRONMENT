package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 消息通知实体
 */
@Data
@TableName("message_notification")
public class MessageNotification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String title;
    private String content;
    private Integer type;
    private Integer isRead;
    private Long relatedId;
    private Date readTime;
    private Date createTime;
}
