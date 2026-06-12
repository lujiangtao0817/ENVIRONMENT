package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 公众投诉实体
 */
@Data
@TableName("complaint")
public class Complaint {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String title;
    private Integer complaintType;
    private String province;
    private String city;
    private String addressDetail;
    private String description;
    private Integer status;
    private Long handlerId;
    private String handleResult;
    private Date handleTime;
    private Date createTime;
}
