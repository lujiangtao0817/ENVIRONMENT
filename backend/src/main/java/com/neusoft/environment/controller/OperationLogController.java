package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.entity.OperationLog;
import com.neusoft.environment.service.OperationLogService;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/log")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    /** 查询操作日志（管理员） */
    @GetMapping("/list")
    public ResultVO<Page<OperationLog>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType) {
        return ResultVO.success(operationLogService.getLogList(page, size, module, operationType));
    }
}
