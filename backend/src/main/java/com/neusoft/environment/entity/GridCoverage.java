package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 网格覆盖率统计实体
 */
@Data
@TableName("grid_coverage")
public class GridCoverage {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 统计日期 */
    private Date statDate;

    /** 全国省份总数 */
    private Integer totalProvinces;

    /** 已覆盖省份数 */
    private Integer coveredProvinces;

    /** 106个大城市总数 */
    private Integer totalCities;

    /** 已覆盖大城市数 */
    private Integer coveredCities;

    /** 省覆盖率% */
    private BigDecimal provinceRate;

    /** 大城市覆盖率% */
    private BigDecimal cityRate;
}
