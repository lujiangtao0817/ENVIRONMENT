package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志实体
 */
@Data
@TableName("operation_log")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String userName;
    private Integer role;
    private String module;
    private String operationType;
    private String operationDesc;
    private String targetTable;
    private Long targetId;
    private String ipAddress;
    private Date createTime;
}
