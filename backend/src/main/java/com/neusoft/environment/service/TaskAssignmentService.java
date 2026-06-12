package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.TaskAssignment;
import com.neusoft.environment.entity.SysUser;
import com.neusoft.environment.vo.FeedbackVO;

import java.util.List;

/**
 * 任务指派服务接口
 */
public interface TaskAssignmentService extends IService<TaskAssignment> {

    /**
     * 管理员指派网格员
     * 优先本地指派 -> 异地就近指派
     */
    void assignGridMan(Long feedbackId, Long gridManId);

    /**
     * 自动推荐最佳网格员
     * 优先本地 -> 同省其他市 -> 全国
     */
    SysUser recommendGridMan(Long feedbackId);

    /**
     * 获取可用的网格员列表
     */
    List<SysUser> getAvailableGridMen(String province, String city);

    /**
     * 网格员：浏览指派给我的任务列表
     */
    List<FeedbackVO> getMyTasks(Long gridManId);

    /**
     * 网格员：浏览任务详情
     */
    FeedbackVO getTaskDetail(Long taskAssignmentId);
}
