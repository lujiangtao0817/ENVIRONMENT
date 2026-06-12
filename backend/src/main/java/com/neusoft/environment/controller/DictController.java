package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.environment.entity.SysDict;
import com.neusoft.environment.mapper.SysDictMapper;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    @Resource
    private SysDictMapper dictMapper;

    /** 根据字典编码获取字典列表 */
    @GetMapping("/{dictCode}")
    public ResultVO<List<SysDict>> getByCode(@PathVariable String dictCode) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDict::getDictCode, dictCode).orderByAsc(SysDict::getSortOrder);
        return ResultVO.success(dictMapper.selectList(wrapper));
    }
}
