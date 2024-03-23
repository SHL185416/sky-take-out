package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理接口
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐信息")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息: {}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 新增套餐  不需要删除缓存
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CachePut(cacheNames = "setmealCache", key = "#setmealDTO.id")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除套餐 不需要删除缓存
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除套餐：{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 更新套餐
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐：{}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("更新套餐状态")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result StartOrStop(@PathVariable Integer status, Long id) {
        log.info("套餐启用/禁用：{}，{}", status, id);
        setmealService.StartOrStop(status, id);
        return Result.success();
    }
}
