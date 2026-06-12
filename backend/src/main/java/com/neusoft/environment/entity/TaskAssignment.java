package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 任务指派实体
 */
@Data
@TableName("task_assignment")
public class TaskAssignment {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 反馈ID */
    private Long feedbackId;

    /** 网格员ID */
    private Long gridManId;

    /** 指派类型 0本地指派 1异地指派 */
    private Integer assignType;

    /** 指派时间 */
    private Date assignTime;
}
