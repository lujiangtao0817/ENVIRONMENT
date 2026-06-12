package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 网格巡检记录实体
 */
@Data
@TableName("grid_inspection_record")
public class GridInspectionRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long gridManId;
    private String province;
    private String city;
    private String addressDetail;
    private Date inspectionDate;
    private String weather;
    private BigDecimal so2Value;
    private BigDecimal coValue;
    private BigDecimal pm25Value;
    private Integer aqiLevel;
    private String description;
    private String remark;
    private Date createTime;
}
