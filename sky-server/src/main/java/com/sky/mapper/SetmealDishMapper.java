package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);


    /**
     * 根据套餐id查询套餐菜品
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 批量插入套餐菜品
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id获取菜品id
     * @param setmealIds
     * @return
     */
    List<Long> getDishIdsBySetmealIds(List<Long> setmealIds);

    /**
     * 删除根据套餐id套餐菜品
     * @param setmealIds
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 删除根据套餐id套餐菜品
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
