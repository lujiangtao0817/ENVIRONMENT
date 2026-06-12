package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.EnvironmentalPolicy;

public interface EnvironmentalPolicyService extends IService<EnvironmentalPolicy> {
    Page<EnvironmentalPolicy> getPublishedList(Integer page, Integer size, Integer policyType);
    EnvironmentalPolicy getDetailAndIncrView(Long id);
    void publish(Long id);
}
