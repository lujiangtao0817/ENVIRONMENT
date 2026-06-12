package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.dto.AqiSubmitRequest;
import com.neusoft.environment.entity.AqiData;
import com.neusoft.environment.vo.AqiDataVO;

/**
 * AQI数据服务接口
 */
public interface AqiDataService extends IService<AqiData> {

    /**
     * 网格员提交实测AQI数据
     */
    void submitAqiData(Long gridManId, AqiSubmitRequest request);

    /**
     * 管理员：分页查询AQI数据列表
     */
    Page<AqiDataVO> getAqiDataList(Integer page, Integer size, String province, String city);

    /**
     * 获取AQI数据详情
     */
    AqiDataVO getAqiDataDetail(Long id);
}
