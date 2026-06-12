package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.dto.FeedbackSubmitRequest;
import com.neusoft.environment.entity.SupervisionFeedback;
import com.neusoft.environment.vo.FeedbackVO;

/**
 * 监督反馈服务接口
 */
public interface FeedbackService extends IService<SupervisionFeedback> {

    /**
     * 提交监督反馈
     */
    void submitFeedback(Long supervisorId, FeedbackSubmitRequest request);

    /**
     * 查询我的历史反馈
     */
    Page<FeedbackVO> getMyFeedback(Long supervisorId, Integer page, Integer size);

    /**
     * 管理员：分页查询反馈列表（支持条件查询）
     */
    Page<FeedbackVO> getFeedbackList(Integer page, Integer size, String province, String city, Integer status);

    /**
     * 获取反馈详情
     */
    FeedbackVO getFeedbackDetail(Long feedbackId);
}
