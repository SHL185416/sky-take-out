package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐业务层
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据id查询套餐信息，同时查询套餐对应的菜品信息
     *
     * @param id
     * @return
     */
    @Transactional
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        String categoryName = categoryMapper.getNameById(setmeal.getCategoryId());
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setCategoryName(categoryName);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 保存套餐信息，同时保存套餐和菜品的关联关系
     *
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null || setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }


    }

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ids删除套餐
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        List<Long> dishIds = setmealDishMapper.getDishIdsBySetmealIds(ids);
        setmealMapper.deleteByIds(ids);
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 更新套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        Long setmealId = setmealDTO.getId();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        setmealDishMapper.deleteBySetmealId(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 根据id设置套餐状态
     *
     * @param status
     * @param id
     */
    public void StartOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal().builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);

    }
}
