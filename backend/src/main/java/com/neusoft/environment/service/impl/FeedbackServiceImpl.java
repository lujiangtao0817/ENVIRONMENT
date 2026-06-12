package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.dto.FeedbackSubmitRequest;
import com.neusoft.environment.entity.SupervisionFeedback;
import com.neusoft.environment.entity.SysUser;
import com.neusoft.environment.mapper.FeedbackMapper;
import com.neusoft.environment.mapper.UserMapper;
import com.neusoft.environment.service.FeedbackService;
import com.neusoft.environment.vo.FeedbackVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监督反馈服务实现
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, SupervisionFeedback> implements FeedbackService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void submitFeedback(Long supervisorId, FeedbackSubmitRequest request) {
        SupervisionFeedback feedback = new SupervisionFeedback();
        feedback.setSupervisorId(supervisorId);
        feedback.setProvince(request.getProvince());
        feedback.setCity(request.getCity());
        feedback.setAddressDetail(request.getAddressDetail());
        feedback.setEstimatedAqiLevel(request.getEstimatedAqiLevel());
        feedback.setDescription(request.getDescription());
        feedback.setStatus(0); // 待指派
        this.save(feedback);
    }

    @Override
    public Page<FeedbackVO> getMyFeedback(Long supervisorId, Integer page, Integer size) {
        LambdaQueryWrapper<SupervisionFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupervisionFeedback::getSupervisorId, supervisorId)
               .orderByDesc(SupervisionFeedback::getCreateTime);

        Page<SupervisionFeedback> resultPage = this.page(new Page<>(page, size), wrapper);
        Page<FeedbackVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<FeedbackVO> voList = resultPage.getRecords().stream()
                .map(FeedbackVO::fromEntity)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public Page<FeedbackVO> getFeedbackList(Integer page, Integer size, String province, String city, Integer status) {
        LambdaQueryWrapper<SupervisionFeedback> wrapper = new LambdaQueryWrapper<>();
        if (province != null && !province.isEmpty()) {
            wrapper.eq(SupervisionFeedback::getProvince, province);
        }
        if (city != null && !city.isEmpty()) {
            wrapper.eq(SupervisionFeedback::getCity, city);
        }
        if (status != null) {
            wrapper.eq(SupervisionFeedback::getStatus, status);
        }
        wrapper.orderByDesc(SupervisionFeedback::getCreateTime);

        Page<SupervisionFeedback> resultPage = this.page(new Page<>(page, size), wrapper);
        Page<FeedbackVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<FeedbackVO> voList = resultPage.getRecords().stream()
                .map(fb -> {
                    FeedbackVO vo = FeedbackVO.fromEntity(fb);
                    // 填充监督员姓名
                    SysUser supervisor = userMapper.selectById(fb.getSupervisorId());
                    if (supervisor != null) {
                        vo.setSupervisorName(supervisor.getRealName());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public FeedbackVO getFeedbackDetail(Long feedbackId) {
        SupervisionFeedback fb = this.getById(feedbackId);
        if (fb == null) {
            throw new IllegalArgumentException("反馈数据不存在");
        }
        FeedbackVO vo = FeedbackVO.fromEntity(fb);
        // 填充监督员姓名
        SysUser supervisor = userMapper.selectById(fb.getSupervisorId());
        if (supervisor != null) {
            vo.setSupervisorName(supervisor.getRealName());
        }
        return vo;
    }
}
