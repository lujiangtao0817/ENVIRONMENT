package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.Complaint;

public interface ComplaintService extends IService<Complaint> {
    void submitComplaint(Complaint complaint, Long userId);
    Page<Complaint> getMyComplaints(Long userId, Integer page, Integer size);
    Page<Complaint> getPendingList(Integer page, Integer size);
    void handle(Long id, Long handlerId, String result, Integer status);
}
