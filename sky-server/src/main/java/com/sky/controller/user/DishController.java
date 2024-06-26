package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, List<DishVO>> redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 类别id
     * @return 菜品列表
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 查看redis中是否有缓存
        String key = "dish_" + categoryId;
        List<DishVO> dishs = redisTemplate.opsForValue().get(key);
        // 如果有缓存，直接返回缓存
        if (dishs != null && dishs.size() > 0) {
            return Result.success(dishs);
        }
        // 没有缓存，查询数据库
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        dishs = dishService.listWithFlavor(dish);
        //放入缓存
        redisTemplate.opsForValue().set(key, dishs);
        return Result.success(dishs);
    }

}
