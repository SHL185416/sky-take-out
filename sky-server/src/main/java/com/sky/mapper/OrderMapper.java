package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询历史订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 统计订单状态
     *
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 获取待支付超时订单
     *
     * @param status
     * @param now
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{now}")
    List<Orders> getOrderByStatusAndTime(Integer status, LocalDateTime now);

    /**
     * 获取指定日期段的营业额
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Double getTurnoverByDate(LocalDateTime dateStart, LocalDateTime dateEnd, Integer status);

    /**
     * 获取指定日期段的订单
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Integer getOrderByDate(LocalDateTime dateStart, LocalDateTime dateEnd, Integer status);


    /**
     * 统计指定日期段的销量top10
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime dateStart, LocalDateTime dateEnd);

}
