package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.GridInspectionRecord;
import com.neusoft.environment.mapper.GridInspectionRecordMapper;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/inspection")
public class GridInspectionController {

    @Resource
    private GridInspectionRecordMapper recordMapper;

    /** 网格员提交巡检记录 */
    @PostMapping("/submit")
    public ResultVO<?> submit(@RequestBody GridInspectionRecord record, Authentication authentication) {
        Long gridManId = (Long) authentication.getPrincipal();
        record.setGridManId(gridManId);
        recordMapper.insert(record);
        return ResultVO.success("巡检记录提交成功");
    }

    /** 我的巡检记录 */
    @GetMapping("/my")
    public ResultVO<Page<GridInspectionRecord>> getMyRecords(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long gridManId = (Long) authentication.getPrincipal();
        LambdaQueryWrapper<GridInspectionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GridInspectionRecord::getGridManId, gridManId)
               .orderByDesc(GridInspectionRecord::getInspectionDate);
        Page<GridInspectionRecord> result = recordMapper.selectPage(new Page<>(page, size), wrapper);
        return ResultVO.success(result);
    }
}
