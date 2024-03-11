package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 根据id查询套餐信息和菜品信息
     *
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 保存套餐信息和菜品信息
     *
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 根据id删除套餐信息和菜品信息
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id删除套餐信息和菜品信息
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 更新套餐信息和菜品信息
     *
     * @param setmealDTO
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 根据id启用或禁用套餐
     *
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);
}
