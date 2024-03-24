package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 添加到购物车
     *
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 展示购物车列表
     *
     * @param
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

    /**
     * 减少购物车数量
     *
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
