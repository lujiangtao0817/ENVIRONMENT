package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.OperationLog;

public interface OperationLogService extends IService<OperationLog> {
    void log(Long userId, String userName, Integer role, String module, String operationType, String desc, String targetTable, Long targetId);
    Page<OperationLog> getLogList(Integer page, Integer size, String module, String operationType);
}
