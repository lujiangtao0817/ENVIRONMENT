package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.dto.AqiSubmitRequest;
import com.neusoft.environment.entity.*;
import com.neusoft.environment.mapper.AqiDataMapper;
import com.neusoft.environment.mapper.FeedbackMapper;
import com.neusoft.environment.mapper.TaskAssignmentMapper;
import com.neusoft.environment.mapper.UserMapper;
import com.neusoft.environment.service.AqiDataService;
import com.neusoft.environment.utils.AqiCalculator;
import com.neusoft.environment.vo.AqiDataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AQI数据服务实现
 */
@Service
public class AqiDataServiceImpl extends ServiceImpl<AqiDataMapper, AqiData> implements AqiDataService {

    @Resource
    private AqiCalculator aqiCalculator;

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private TaskAssignmentMapper taskAssignmentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void submitAqiData(Long gridManId, AqiSubmitRequest request) {
        // 验证反馈是否存在
        SupervisionFeedback feedback = feedbackMapper.selectById(request.getFeedbackId());
        if (feedback == null) {
            throw new IllegalArgumentException("反馈任务不存在");
        }
        if (feedback.getStatus() != 1) {
            throw new IllegalArgumentException("该反馈任务状态不正确，必须为已指派状态");
        }

        // 验证是否已指派给当前网格员
        LambdaQueryWrapper<TaskAssignment> taWrapper = new LambdaQueryWrapper<>();
        taWrapper.eq(TaskAssignment::getFeedbackId, request.getFeedbackId())
                 .eq(TaskAssignment::getGridManId, gridManId);
        if (taskAssignmentMapper.selectCount(taWrapper) == 0) {
            throw new IllegalArgumentException("该任务未指派给您");
        }

        // 验证是否已提交过
        LambdaQueryWrapper<AqiData> aqiWrapper = new LambdaQueryWrapper<>();
        aqiWrapper.eq(AqiData::getFeedbackId, request.getFeedbackId());
        if (this.count(aqiWrapper) > 0) {
            throw new IllegalArgumentException("该任务已提交过AQI数据");
        }

        // 计算最终AQI
        java.math.BigDecimal finalAqi = aqiCalculator.calculateFinalAqi(
                request.getSo2Aqi(), request.getCoAqi(), request.getPm25Aqi());

        // 判断是否超标
        boolean exceeded = aqiCalculator.isExceeded(finalAqi);

        // 保存AQI数据
        AqiData aqiData = new AqiData();
        aqiData.setFeedbackId(request.getFeedbackId());
        aqiData.setGridManId(gridManId);
        aqiData.setSo2Aqi(request.getSo2Aqi());
        aqiData.setCoAqi(request.getCoAqi());
        aqiData.setPm25Aqi(request.getPm25Aqi());
        aqiData.setFinalAqi(finalAqi);
        aqiData.setIsExceeded(exceeded ? 1 : 0);
        this.save(aqiData);

        // 更新反馈状态为已完成
        feedback.setStatus(2);
        feedbackMapper.updateById(feedback);

        // 更新网格员状态为空闲
        SysUser gridMan = userMapper.selectById(gridManId);
        if (gridMan != null) {
            gridMan.setWorkStatus(0);
            userMapper.updateById(gridMan);
        }
    }

    @Override
    public Page<AqiDataVO> getAqiDataList(Integer page, Integer size, String province, String city) {
        Page<AqiData> resultPage = this.page(new Page<>(page, size));
        Page<AqiDataVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<AqiDataVO> voList = resultPage.getRecords().stream()
                .map(ad -> {
                    AqiDataVO vo = AqiDataVO.fromEntity(ad);
                    SysUser gridMan = userMapper.selectById(ad.getGridManId());
                    if (gridMan != null) {
                        vo.setGridManName(gridMan.getRealName());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public AqiDataVO getAqiDataDetail(Long id) {
        AqiData ad = this.getById(id);
        if (ad == null) {
            throw new IllegalArgumentException("AQI数据不存在");
        }
        AqiDataVO vo = AqiDataVO.fromEntity(ad);
        SysUser gridMan = userMapper.selectById(ad.getGridManId());
        if (gridMan != null) {
            vo.setGridManName(gridMan.getRealName());
        }
        return vo;
    }
}
