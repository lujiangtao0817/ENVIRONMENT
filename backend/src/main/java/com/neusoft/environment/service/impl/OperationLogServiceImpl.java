package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.OperationLog;
import com.neusoft.environment.mapper.OperationLogMapper;
import com.neusoft.environment.service.OperationLogService;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public void log(Long userId, String userName, Integer role, String module, String operationType, String desc, String targetTable, Long targetId) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUserName(userName);
        log.setRole(role);
        log.setModule(module);
        log.setOperationType(operationType);
        log.setOperationDesc(desc);
        log.setTargetTable(targetTable);
        log.setTargetId(targetId);
        this.save(log);
    }

    @Override
    public Page<OperationLog> getLogList(Integer page, Integer size, String module, String operationType) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (module != null) wrapper.eq(OperationLog::getModule, module);
        if (operationType != null) wrapper.eq(OperationLog::getOperationType, operationType);
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }
}
