package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 反馈图片实体
 */
@Data
@TableName("feedback_image")
public class FeedbackImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long feedbackId;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer sortOrder;
    private Date createTime;
}
