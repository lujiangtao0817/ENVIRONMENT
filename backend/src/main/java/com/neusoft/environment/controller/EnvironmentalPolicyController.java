package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.entity.EnvironmentalPolicy;
import com.neusoft.environment.service.EnvironmentalPolicyService;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/policy")
public class EnvironmentalPolicyController {

    @Resource
    private EnvironmentalPolicyService policyService;

    /** 获取已发布政策列表 */
    @GetMapping("/list")
    public ResultVO<Page<EnvironmentalPolicy>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer policyType) {
        return ResultVO.success(policyService.getPublishedList(page, size, policyType));
    }

    /** 政策详情（含浏览量+1） */
    @GetMapping("/detail/{id}")
    public ResultVO<EnvironmentalPolicy> getDetail(@PathVariable Long id) {
        return ResultVO.success(policyService.getDetailAndIncrView(id));
    }
}
