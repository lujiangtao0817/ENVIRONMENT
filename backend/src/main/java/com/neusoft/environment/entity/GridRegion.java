package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 网格区域实体
 */
@Data
@TableName("grid_region")
public class GridRegion {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 城市分类 */
    private String cityType;

    /** 是否省会 0否 1是 */
    private Integer isCapital;
}
