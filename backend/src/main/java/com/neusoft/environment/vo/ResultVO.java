package com.neusoft.environment.vo;

import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class ResultVO<T> {

    /** 状态码 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /** 数据 */
    private T data;

    public static <T> ResultVO<T> success() {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultVO<T> success(String msg, T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> ResultVO<T> error(Integer code, String msg) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
