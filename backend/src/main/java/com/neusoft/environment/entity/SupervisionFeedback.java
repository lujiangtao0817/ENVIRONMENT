package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 公众监督反馈实体
 */
@Data
@TableName("supervision_feedback")
public class SupervisionFeedback {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 监督员ID */
    private Long supervisorId;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 具体地址 */
    private String addressDetail;

    /** 预估AQI等级(1-6) */
    private Integer estimatedAqiLevel;

    /** 空气质量描述信息 */
    private String description;

    /** 状态 0待指派 1已指派 2已完成 */
    private Integer status;

    /** 反馈时间 */
    private Date createTime;
}
