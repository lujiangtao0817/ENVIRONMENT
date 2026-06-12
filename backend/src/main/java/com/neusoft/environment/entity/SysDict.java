package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统字典实体
 */
@Data
@TableName("sys_dict")
public class SysDict {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String dictCode;
    private String dictName;
    private String dictValue;
    private String dictLabel;
    private Integer sortOrder;
    private String remark;
    private Date createTime;
}
