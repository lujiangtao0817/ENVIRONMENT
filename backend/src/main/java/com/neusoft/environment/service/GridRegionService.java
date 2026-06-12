package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.GridRegion;

import java.util.List;
import java.util.Map;

/**
 * 网格区域服务接口
 */
public interface GridRegionService extends IService<GridRegion> {

    /**
     * 获取所有省份列表
     */
    List<String> getProvinces();

    /**
     * 根据省获取城市列表
     */
    List<String> getCities(String province);

    /**
     * 获取完整的省市树形结构
     */
    List<Map<String, Object>> getRegionTree();
}
