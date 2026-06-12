package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.*;
import com.neusoft.environment.mapper.*;
import com.neusoft.environment.service.TaskAssignmentService;
import com.neusoft.environment.vo.FeedbackVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务指派服务实现
 */
@Service
public class TaskAssignmentServiceImpl extends ServiceImpl<TaskAssignmentMapper, TaskAssignment> implements TaskAssignmentService {

    @Resource
    private FeedbackMapper feedbackMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public void assignGridMan(Long feedbackId, Long gridManId) {
        // 验证反馈数据
        SupervisionFeedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new IllegalArgumentException("反馈数据不存在");
        }
        if (feedback.getStatus() != 0) {
            throw new IllegalArgumentException("该反馈数据已被指派或已完成");
        }

        // 验证网格员
        SysUser gridMan = userMapper.selectById(gridManId);
        if (gridMan == null || gridMan.getRole() != 1) {
            throw new IllegalArgumentException("网格员不存在或身份不正确");
        }
        if (gridMan.getWorkStatus() == 1) {
            throw new IllegalArgumentException("该网格员当前正忙，请选择其他空闲网格员");
        }

        // 判定指派类型
        int assignType = 0; // 本地指派
        if (feedback.getProvince().equals(gridMan.getGridProvince())
                && feedback.getCity().equals(gridMan.getGridCity())) {
            assignType = 0; // 本地指派
        } else {
            assignType = 1; // 异地指派
        }

        // 保存指派记录
        TaskAssignment assignment = new TaskAssignment();
        assignment.setFeedbackId(feedbackId);
        assignment.setGridManId(gridManId);
        assignment.setAssignType(assignType);
        assignment.setAssignTime(new Date());
        this.save(assignment);

        // 更新反馈状态
        feedback.setStatus(1); // 已指派
        feedbackMapper.updateById(feedback);

        // 更新网格员状态为忙碌
        gridMan.setWorkStatus(1);
        userMapper.updateById(gridMan);
    }

    @Override
    public SysUser recommendGridMan(Long feedbackId) {
        SupervisionFeedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new IllegalArgumentException("反馈数据不存在");
        }

        String province = feedback.getProvince();
        String city = feedback.getCity();

        // 1. 优先本地指派：同省同市空闲网格员
        LambdaQueryWrapper<SysUser> localWrapper = new LambdaQueryWrapper<>();
        localWrapper.eq(SysUser::getRole, 1)
                    .eq(SysUser::getGridProvince, province)
                    .eq(SysUser::getGridCity, city)
                    .eq(SysUser::getWorkStatus, 0);
        SysUser localGridMan = userMapper.selectOne(localWrapper);
        if (localGridMan != null) {
            return localGridMan;
        }

        // 2. 同省异地指派：同省其他市空闲网格员
        LambdaQueryWrapper<SysUser> sameProvinceWrapper = new LambdaQueryWrapper<>();
        sameProvinceWrapper.eq(SysUser::getRole, 1)
                           .eq(SysUser::getGridProvince, province)
                           .ne(SysUser::getGridCity, city)
                           .eq(SysUser::getWorkStatus, 0);
        SysUser sameProvinceGridMan = userMapper.selectOne(sameProvinceWrapper);
        if (sameProvinceGridMan != null) {
            return sameProvinceGridMan;
        }

        // 3. 全国范围就近指派
        LambdaQueryWrapper<SysUser> nationalWrapper = new LambdaQueryWrapper<>();
        nationalWrapper.eq(SysUser::getRole, 1)
                       .eq(SysUser::getWorkStatus, 0);
        return userMapper.selectOne(nationalWrapper);
    }

    @Override
    public List<SysUser> getAvailableGridMen(String province, String city) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getRole, 1);

        // 如果指定了省市，优先返回本地网格员
        if (province != null && city != null) {
            // 本地优先
            wrapper.and(w -> w
                .eq(SysUser::getGridProvince, province)
                .eq(SysUser::getGridCity, city)
            ).or().eq(SysUser::getWorkStatus, 0);
        } else {
            wrapper.eq(SysUser::getWorkStatus, 0);
        }

        wrapper.eq(SysUser::getWorkStatus, 0);
        return userMapper.selectList(wrapper);
    }

    @Override
    public List<FeedbackVO> getMyTasks(Long gridManId) {
        // 查询指派给该网格员的未完成任务
        LambdaQueryWrapper<TaskAssignment> taWrapper = new LambdaQueryWrapper<>();
        taWrapper.eq(TaskAssignment::getGridManId, gridManId);

        List<TaskAssignment> assignments = this.list(taWrapper);
        List<FeedbackVO> result = new ArrayList<>();

        for (TaskAssignment ta : assignments) {
            SupervisionFeedback fb = feedbackMapper.selectById(ta.getFeedbackId());
            if (fb != null && fb.getStatus() == 1) { // 只返回已指派但未完成的
                FeedbackVO vo = FeedbackVO.fromEntity(fb);
                SysUser supervisor = userMapper.selectById(fb.getSupervisorId());
                if (supervisor != null) {
                    vo.setSupervisorName(supervisor.getRealName());
                }
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public FeedbackVO getTaskDetail(Long feedbackId) {
        SupervisionFeedback fb = feedbackMapper.selectById(feedbackId);
        if (fb == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        FeedbackVO vo = FeedbackVO.fromEntity(fb);
        SysUser supervisor = userMapper.selectById(fb.getSupervisorId());
        if (supervisor != null) {
            vo.setSupervisorName(supervisor.getRealName());
        }
        return vo;
    }
}
