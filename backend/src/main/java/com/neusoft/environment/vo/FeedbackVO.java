package com.neusoft.environment.vo;

import com.neusoft.environment.entity.SupervisionFeedback;
import lombok.Data;

/**
 * 反馈信息视图
 */
@Data
public class FeedbackVO {

    private Long id;
    private Long supervisorId;
    private String supervisorName;
    private String province;
    private String city;
    private String addressDetail;
    private Integer estimatedAqiLevel;
    private String estimatedAqiLevelName;
    private String description;
    private Integer status;
    private String statusName;
    private String createTime;

    /** 从实体构建 */
    public static FeedbackVO fromEntity(SupervisionFeedback fb) {
        FeedbackVO vo = new FeedbackVO();
        vo.setId(fb.getId());
        vo.setSupervisorId(fb.getSupervisorId());
        vo.setProvince(fb.getProvince());
        vo.setCity(fb.getCity());
        vo.setAddressDetail(fb.getAddressDetail());
        vo.setEstimatedAqiLevel(fb.getEstimatedAqiLevel());
        vo.setEstimatedAqiLevelName(getLevelName(fb.getEstimatedAqiLevel()));
        vo.setDescription(fb.getDescription());
        vo.setStatus(fb.getStatus());
        vo.setStatusName(getStatusName(fb.getStatus()));
        vo.setCreateTime(fb.getCreateTime() != null ? fb.getCreateTime().toString() : null);
        return vo;
    }

    private static String getLevelName(int level) {
        switch (level) {
            case 1: return "优";
            case 2: return "良";
            case 3: return "轻度污染";
            case 4: return "中度污染";
            case 5: return "重度污染";
            case 6: return "严重污染";
            default: return "未知";
        }
    }

    private static String getStatusName(int status) {
        switch (status) {
            case 0: return "待指派";
            case 1: return "已指派";
            case 2: return "已完成";
            default: return "未知";
        }
    }
}
