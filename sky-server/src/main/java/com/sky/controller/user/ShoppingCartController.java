package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端-购物车管理
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Api("C端-购物车管理相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;


    /**
     * 添加购物车物品
     *
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车物品")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车物品：{}", shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 获取购物车列表
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取购物车列表")
    public Result<List<ShoppingCart>> list() {
        log.info("用户id: {} 获取购物车列表的", BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean() {
        log.info("用户id: {} 清空购物车", BaseContext.getCurrentId());
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 减少购物车物品
     */
    @PostMapping("/sub")
    @ApiOperation("减少购物车物品")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("减少购物车物品：{}", shoppingCartDTO);
        shoppingCartService.sub(shoppingCartDTO);
        return Result.success();

    }
}
