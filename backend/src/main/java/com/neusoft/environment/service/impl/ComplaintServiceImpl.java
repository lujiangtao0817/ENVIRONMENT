package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.Complaint;
import com.neusoft.environment.mapper.ComplaintMapper;
import com.neusoft.environment.service.ComplaintService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ComplaintServiceImpl extends ServiceImpl<ComplaintMapper, Complaint> implements ComplaintService {

    @Override
    public void submitComplaint(Complaint complaint, Long userId) {
        complaint.setUserId(userId);
        complaint.setStatus(0);
        this.save(complaint);
    }

    @Override
    public Page<Complaint> getMyComplaints(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Complaint::getUserId, userId).orderByDesc(Complaint::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public Page<Complaint> getPendingList(Integer page, Integer size) {
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Complaint::getStatus).orderByDesc(Complaint::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void handle(Long id, Long handlerId, String result, Integer status) {
        Complaint c = this.getById(id);
        if (c == null) throw new IllegalArgumentException("投诉不存在");
        c.setHandlerId(handlerId);
        c.setHandleResult(result);
        c.setStatus(status);
        c.setHandleTime(new Date());
        this.updateById(c);
    }
}
