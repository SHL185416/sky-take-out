package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService {

    /**
     * 保存菜品信息，包括口味信息
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品信息
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品信息，包括口味信息
     *
     * @param ids
     */
    public void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息和口味信息
     *
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品列表
     *
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);
}
