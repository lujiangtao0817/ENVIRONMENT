package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.EnvironmentalPolicy;
import com.neusoft.environment.mapper.EnvironmentalPolicyMapper;
import com.neusoft.environment.service.EnvironmentalPolicyService;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentalPolicyServiceImpl extends ServiceImpl<EnvironmentalPolicyMapper, EnvironmentalPolicy> implements EnvironmentalPolicyService {

    @Override
    public Page<EnvironmentalPolicy> getPublishedList(Integer page, Integer size, Integer policyType) {
        LambdaQueryWrapper<EnvironmentalPolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnvironmentalPolicy::getStatus, 1);
        if (policyType != null) {
            wrapper.eq(EnvironmentalPolicy::getPolicyType, policyType);
        }
        wrapper.orderByDesc(EnvironmentalPolicy::getPublishDate);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public EnvironmentalPolicy getDetailAndIncrView(Long id) {
        EnvironmentalPolicy policy = this.getById(id);
        if (policy == null) throw new IllegalArgumentException("政策不存在");
        policy.setViewCount(policy.getViewCount() + 1);
        this.updateById(policy);
        return policy;
    }

    @Override
    public void publish(Long id) {
        EnvironmentalPolicy policy = this.getById(id);
        if (policy == null) throw new IllegalArgumentException("政策不存在");
        policy.setStatus(1);
        this.updateById(policy);
    }
}
