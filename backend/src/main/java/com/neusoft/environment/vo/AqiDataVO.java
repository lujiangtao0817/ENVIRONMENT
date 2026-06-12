package com.neusoft.environment.vo;

import com.neusoft.environment.entity.AqiData;
import lombok.Data;

import java.math.BigDecimal;

/**
 * AQI数据视图
 */
@Data
public class AqiDataVO {

    private Long id;
    private Long feedbackId;
    private Long gridManId;
    private String gridManName;
    private BigDecimal so2Aqi;
    private BigDecimal coAqi;
    private BigDecimal pm25Aqi;
    private BigDecimal finalAqi;
    private Integer isExceeded;
    private String exceedStatus;
    private Integer aqiLevel;
    private String aqiLevelName;
    private String createTime;

    public static AqiDataVO fromEntity(AqiData ad) {
        AqiDataVO vo = new AqiDataVO();
        vo.setId(ad.getId());
        vo.setFeedbackId(ad.getFeedbackId());
        vo.setGridManId(ad.getGridManId());
        vo.setSo2Aqi(ad.getSo2Aqi());
        vo.setCoAqi(ad.getCoAqi());
        vo.setPm25Aqi(ad.getPm25Aqi());
        vo.setFinalAqi(ad.getFinalAqi());
        vo.setIsExceeded(ad.getIsExceeded());
        vo.setExceedStatus(ad.getIsExceeded() == 1 ? "超标" : "良好");
        vo.setAqiLevel(getLevel(ad.getFinalAqi()));
        vo.setAqiLevelName(getLevelName(vo.getAqiLevel()));
        vo.setCreateTime(ad.getCreateTime() != null ? ad.getCreateTime().toString() : null);
        return vo;
    }

    private static int getLevel(BigDecimal aqi) {
        double v = aqi.doubleValue();
        if (v <= 50) return 1;
        if (v <= 100) return 2;
        if (v <= 150) return 3;
        if (v <= 200) return 4;
        if (v <= 300) return 5;
        return 6;
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
}
