package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.transaction.annotation.Transactional;

public interface DishService {

    /**
     * 保存菜品信息，包括口味信息
     */
    @Transactional
    void saveWithFlavor(DishDTO dishDTO);
}
