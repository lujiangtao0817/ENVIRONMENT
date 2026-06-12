package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.GridRegion;
import com.neusoft.environment.mapper.GridRegionMapper;
import com.neusoft.environment.service.GridRegionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 网格区域服务实现
 */
@Service
public class GridRegionServiceImpl extends ServiceImpl<GridRegionMapper, GridRegion> implements GridRegionService {

    @Override
    public List<String> getProvinces() {
        LambdaQueryWrapper<GridRegion> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(GridRegion::getProvince).groupBy(GridRegion::getProvince);
        return this.list(wrapper).stream()
                .map(GridRegion::getProvince)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCities(String province) {
        LambdaQueryWrapper<GridRegion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GridRegion::getProvince, province)
               .select(GridRegion::getCity);
        return this.list(wrapper).stream()
                .map(GridRegion::getCity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getRegionTree() {
        List<Map<String, Object>> tree = new ArrayList<>();

        // 按省分组
        LambdaQueryWrapper<GridRegion> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(GridRegion::getProvince).orderByAsc(GridRegion::getIsCapital);
        List<GridRegion> allRegions = this.list(wrapper);

        Map<String, List<GridRegion>> provinceMap = allRegions.stream()
                .collect(Collectors.groupingBy(GridRegion::getProvince, LinkedHashMap::new, Collectors.toList()));

        for (Map.Entry<String, List<GridRegion>> entry : provinceMap.entrySet()) {
            Map<String, Object> provinceNode = new HashMap<>();
            provinceNode.put("province", entry.getKey());
            List<Map<String, Object>> cities = new ArrayList<>();
            for (GridRegion region : entry.getValue()) {
                Map<String, Object> cityNode = new HashMap<>();
                cityNode.put("city", region.getCity());
                cityNode.put("isCapital", region.getIsCapital());
                cityNode.put("cityType", region.getCityType());
                cities.add(cityNode);
            }
            provinceNode.put("cities", cities);
            tree.add(provinceNode);
        }

        return tree;
    }
}
