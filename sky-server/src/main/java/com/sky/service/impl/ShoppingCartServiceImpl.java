package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车业务层
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 添加购物车数据
     *
     * @param shoppingCartDTO
     */
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        // 如果商品在购物车中已经存在，则数量增加
        if (list != null && list.size() > 0) {
            shoppingCart = list.get(0);
            shoppingCart.setNumber(list.get(0).getNumber() + 1);
            shoppingCartMapper.updateNumberByID(shoppingCart);
        } else {
            // 如果商品在购物车中不存在，则添加到购物车
            // 本次添加的菜品
            if (shoppingCart.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 本次添加的套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }

    }

    /**
     * 展示购物车列表
     *
     * @param
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        Long UserId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(UserId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果商品在购物车中已经存在，则数量大于 1
        shoppingCart = list.get(0);
        if (shoppingCart.getNumber() > 1) {
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
            shoppingCartMapper.updateNumberByID(shoppingCart);
        } else {
            // 如果商品在购物车中已经存在，则数量 == 1
            shoppingCartMapper.deleteById(shoppingCart.getId());
        }

    }
}
