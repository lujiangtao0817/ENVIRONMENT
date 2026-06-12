package com.neusoft.environment.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * AQI 计算工具类
 * AQI = MAX(SO2AQI, COAQI, PM2.5AQI)
 * 超标判定：AQI等级 >= 3（轻度污染及以上，即 finalAqi > 100）
 */
@Component
public class AqiCalculator {

    private static final BigDecimal EXCEED_THRESHOLD = new BigDecimal("100");

    /**
     * 计算最终 AQI 值
     * AQI = MAX(SO2AQI, COAQI, PM2.5AQI)
     */
    public BigDecimal calculateFinalAqi(BigDecimal so2Aqi, BigDecimal coAqi, BigDecimal pm25Aqi) {
        BigDecimal max = so2Aqi;
        if (coAqi.compareTo(max) > 0) {
            max = coAqi;
        }
        if (pm25Aqi.compareTo(max) > 0) {
            max = pm25Aqi;
        }
        return max.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 判断是否超标
     * AQI > 100 即为超标（轻度污染及以上）
     */
    public boolean isExceeded(BigDecimal finalAqi) {
        return finalAqi.compareTo(EXCEED_THRESHOLD) > 0;
    }

    /**
     * 根据 AQI 值获取等级
     */
    public int getAqiLevel(BigDecimal finalAqi) {
        double aqi = finalAqi.doubleValue();
        if (aqi <= 50) return 1;
        if (aqi <= 100) return 2;
        if (aqi <= 150) return 3;
        if (aqi <= 200) return 4;
        if (aqi <= 300) return 5;
        return 6;
    }

    /**
     * 获取 AQI 等级名称
     */
    public String getLevelName(int level) {
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
